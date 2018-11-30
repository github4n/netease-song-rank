package me.olook.netease.song.rank.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.*;
import me.olook.netease.song.rank.dto.TemplateMsgKeyWord;
import me.olook.netease.song.rank.dto.TemplateMsgParam;
import me.olook.netease.song.rank.entity.*;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyPoolUtil;
import me.olook.netease.song.rank.util.wechat.WeChatHttpClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 核心流程
 * 1.请求处理数据
 * 2.计算快照比对
 * 3.查找变动歌曲
 * @author zhaohw
 * @date 2018-11-20 14:56
 */
@Slf4j
@Service
public class RecordRankService {

    @Resource
    private TimerJobBiz timerJobBiz;

    @Resource
    private TimerJobRecordBiz timerJobRecordBiz;

    @Resource
    private UserRefJobBiz userRefJobBiz;

    @Resource
    private SongRankDataBiz songRankDataBiz;

    @Resource
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @Resource
    private TemplateMessageBiz templateMessageBiz;

    @Resource
    private AccessTokenInfoBiz accessTokenInfoBiz;

    @Resource
    private WeChatHttpClient weChatHttpClient;

    public void run(String targetUserId){


        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        TimerJob currentJob = timerJobBiz.findByTargetUserId(targetUserId);

        // 使用代理请求数据
        ProxyInfo proxyInfo = ProxyPoolUtil.get();
        JSONObject jsonObject =
                NetEaseHttpClient.getSongRankData(currentJob.getTargetUserId(),proxyInfo);

        boolean dataValid = handleProxy(jsonObject, proxyInfo);
        if(!dataValid){
            // 使用代理未获取到数据  不使用代理重试
            jsonObject = NetEaseHttpClient.getSongRankData(currentJob.getTargetUserId());
            if(jsonObject == null){
                log.error("post for user[{}] retry failed",targetUserId);
                return;
            }
            log.debug("post for user[{}] retry success",targetUserId);
        }
        List<SongRankData> songRankDataList = RecordRankResolver.parseData(jsonObject);
        if(songRankDataList == null){
            return;
        }

        TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());

        if(songRankDataList.size() == 0){
            log.debug("{} {} 执行结束,无周榜数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
            if(oldRecord == null){
                log.info("{} {} 初始空白数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
                timerJobRecordBiz.saveTimerJobRecord(currentJob,uuid);
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(SongRankData data : songRankDataList){
            sb.append(data.toString());
            data.setJobRecordId(uuid);
        }
        String snapshot = sb.toString().hashCode()+"";

        //数据变更
        if(oldRecord == null||!snapshot.equals(oldRecord.getSnapshot())){
            if(oldRecord != null){
                List<SongRankData> oldDataList = songRankDataBiz.getOldDataList(oldRecord.getId());
                SongRankDataDiff saveResult = saveSongRankDataDiff(songRankDataList, oldDataList, uuid, targetUserId);
                if(saveResult != null){
                    log.info("{} {} 数据变更",currentJob.getTargetNickname(),targetUserId);
                    if(saveResult.getIsBatchUpdate()==0){
                        sendTemplates(saveResult);
                    }
                }
            }else {
                log.info("{} {} 初始数据",currentJob.getTargetNickname(),targetUserId);
            }
            songRankDataBiz.saveAndDeleteSongRankData(songRankDataList,oldRecord==null?null:oldRecord.getId());
            timerJobRecordBiz.saveTimerJobRecord(currentJob,uuid,snapshot);
        }

    }


    /**
     * 找出并存储变动数据
     */
    private SongRankDataDiff saveSongRankDataDiff(List<SongRankData> newList,List<SongRankData> oldList
                                                                ,String jobRecordId ,String targetUserId){
        List<SongRankDataDiff> addList = songRankDataDiffBiz.findSongRankDataDiffByRank(newList, oldList);
        int isBatch = 0;
        if(addList.size()==0){
            addList = songRankDataDiffBiz.findSongRankDataDiffByScore(newList,oldList);
        }
        if(addList.size()==0){
            return null;
        }
        if(addList.size()>3){
            isBatch = 1;
        }
        //如果时间为3~5点，判定为系统更新数据
        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        if(3 == hour||4 == hour||5 == hour){
            //日期减一天
            localDateTime = LocalDateTime.now().minus(1,ChronoUnit.DAYS);
            isBatch = 1;
        }
        int finalIsBatch = isBatch;
        Date dateTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        addList.forEach(p->{
            p.setTargetUserId(targetUserId);
            p.setJobRecordId(jobRecordId);
            p.setIsBatchUpdate(finalIsBatch);
            p.setChangeTime(dateTime);
            songRankDataDiffBiz.save(p);
        });
        return addList.get(0);
    }

    private void sendTemplates(SongRankDataDiff dataDiff){
        List<TemplateMessage> templates = templateMessageBiz.findValidTemplates(dataDiff.getTargetUserId());
        if(templates.size()==0){
            return;
        }
        String validToken = accessTokenInfoBiz.getValidAccessToken();
        if(validToken==null){
            log.error("推送模板消息找不到可用 access token");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimerJob timerJob = timerJobBiz.findByTargetUserId(dataDiff.getTargetUserId());
        for(TemplateMessage msg : templates){

            TemplateMsgParam param = new TemplateMsgParam();
            param.setToUser(msg.getOpenid());
            param.setTemplateId(msg.getTemplateId());
            param.setPage(msg.getPage());
            param.setFormId(msg.getFormId());
            TemplateMsgKeyWord keyWord = new TemplateMsgKeyWord(dataDiff.getSong(),sdf.format(new Date()),timerJob.getTargetNickname());
            param.setData(keyWord);

            weChatHttpClient.sendPushTemplate(param, validToken);

            msg.setIsValid(TemplateMessage.INVALID);
            msg.setUpdTime(new Date());
            templateMessageBiz.save(msg);
        }

    }

    private boolean handleProxy(JSONObject data,ProxyInfo proxyInfo){
        if(data == null){
            ProxyPoolUtil.fail(proxyInfo);
            return false;
        }else {
            ProxyPoolUtil.restore(proxyInfo);
            return true;
        }
    }

}
