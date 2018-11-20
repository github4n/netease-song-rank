package me.olook.netease.song.rank.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.*;
import me.olook.netease.song.rank.entity.*;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyPoolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    private SongRankDataBiz songRankDataBiz;

    @Resource
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @Resource
    private TemplateMessageBiz templateMessageBiz;

    public void run(String targetUserId){
        TimerJob currentJob = timerJobBiz.findByTargetUserId(targetUserId);

        ProxyInfo proxyInfo = ProxyPoolUtil.get();
        JSONObject jsonObject =
                NetEaseHttpClient.getSongRankData(currentJob.getTargetUserId(),proxyInfo);

        boolean dataValid = handleProxy(jsonObject, proxyInfo);
        if(!dataValid){
            return;
        }
        List<SongRankData> songRankDataList = RecordRankResolver.parseData(jsonObject);

        TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());

        if(songRankDataList.size() == 0){
            log.debug("{} {} 执行结束,无周榜数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
            if(oldRecord == null){
                saveTimerJobRecord(currentJob);
            }
            return;
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
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
                if(saveResult!=null && saveResult.getIsBatchUpdate()==0){
                    //消息推送
                }
                log.info("{} {} 数据变更",currentJob.getTargetNickname(),targetUserId);
            }else {
                log.info("{} {} 初始数据",currentJob.getTargetNickname(),targetUserId);
            }
        }
        saveAndDeleteSongRankData(songRankDataList,oldRecord==null?null:oldRecord.getId());
        saveTimerJobRecord(currentJob,uuid,snapshot);

    }

    @Transactional(rollbackFor = Exception.class)
    void saveAndDeleteSongRankData(List<SongRankData> songRankDataList, String oldJobRecordId){
        saveSongRankData(songRankDataList);
        deleteOldSongRankData(oldJobRecordId);
    }

    private void saveSongRankData(List<SongRankData> songRankDataList){
        songRankDataList.forEach(songRankDataBiz::save);
    }

    private void deleteOldSongRankData(String oldJobRecordId){
        if(oldJobRecordId!=null){
            songRankDataBiz.deleteLastSongRankData(oldJobRecordId);
        }
    }


    /**
     * 找出并存储变动数据
     */
    private SongRankDataDiff saveSongRankDataDiff(List<SongRankData> newList,List<SongRankData> oldList
                                                                ,String jobRecordId ,String targetUserId){
        List<SongRankDataDiff> addList = findSongRankDataDiff(newList, oldList);
        int isBatch = 0;
        if(addList.size()==0){
            // todo score变化 zhaohw 2018/11/20 16:58
        }
        if(addList.size()>3){
            isBatch = 1;
        }
        //如果时间为3~5点，判定为系统更新数据
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(3 == hour||4 == hour||5 == hour){
            //日期减一天
            c.add(Calendar.DAY_OF_MONTH,-1);
            isBatch = 1;
        }
        int finalIsBatch = isBatch;
        addList.forEach(p->{
            p.setTargetUserId(targetUserId);
            p.setJobRecordId(jobRecordId);
            p.setIsBatchUpdate(finalIsBatch);
            songRankDataDiffBiz.save(p);
        });
        return addList.get(0);
    }

    private List<SongRankDataDiff> findSongRankDataDiff(List<SongRankData> newList,List<SongRankData> oldList){
        Map<Integer,SongRankData> oldMap = new HashMap<Integer, SongRankData>(oldList.size());
        oldList.forEach(o->{
            oldMap.put(o.getSongId(),o);
        });
        List<SongRankDataDiff> diffList = new ArrayList<>();
        newList.forEach(n->{
            if(!oldMap.containsKey(n.getSongId())){
                SongRankDataDiff songRankDataDiff = buildDataDiff(n);
                diffList.add(songRankDataDiff);
            }
            else if(n.getRank()>oldMap.get(n.getSongId()).getRank()){
                SongRankDataDiff songRankDataDiff = buildDataDiff(n,oldMap.get(n.getSongId()).getRank()-n.getRank());
                diffList.add(songRankDataDiff);
            }
        });
        return diffList;
    }

    private SongRankDataDiff buildDataDiff(SongRankData songRankData){
        return this.buildDataDiff(songRankData,-1);
    }

    private SongRankDataDiff buildDataDiff(SongRankData songRankData,Integer rankChange){
        return SongRankDataDiff.builder()
                .songId(songRankData.getSongId())
                .song(songRankData.getSong())
                .singer(songRankData.getSinger())
                .picUrl(songRankData.getPicUrl())
                .rankChange(rankChange)
                .build();
    }

    private void saveTimerJobRecord(TimerJob currentJob){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("{} {} 初始空白数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
        this.saveTimerJobRecord(currentJob,uuid,"-1");
    }

    private void saveTimerJobRecord(TimerJob currentJob,String jobRecordId, String snapshot){
        TimerJobRecord timerJobRecord = TimerJobRecord.builder()
                .startTime(new Date())
                .snapshot(snapshot)
                .newData(1)
                .id(jobRecordId)
                .count(0)
                .jobId(currentJob.getId())
                .endTime(new Date()).build();
        timerJobRecordBiz.save(timerJobRecord);
    }

    private void sendTemplates(SongRankDataDiff songRankDataDiff){
        List<TemplateMessage> validTemplates = templateMessageBiz.findValidTemplates(songRankDataDiff.getTargetUserId());
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
