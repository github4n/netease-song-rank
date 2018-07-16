package me.olook.netease.song.rank.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.biz.*;
import me.olook.netease.song.rank.dto.SongRankDataDTO;
import me.olook.netease.song.rank.entity.*;
import me.olook.netease.song.rank.exception.PermissionDeniedException;
import me.olook.netease.song.rank.util.netease.NeteaseUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author zhaohw
 * @date 2018-02-08 15:08
 */
@TimerJobTypeName(value = "听歌排行爬取任务")
public class SongRankTask implements Job {

    private Logger log = LoggerFactory.getLogger(SongRankTask.class);

    @Autowired
    private TimerJobBiz timerJobBiz ;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Autowired
    private SongRankDataBiz songRankDataBiz;

    @Autowired
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @Autowired
    private TimerJobRecordBiz timerJobRecordBiz;

    @Autowired
    private TemplateMessageBiz templateMessageBiz;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        TimerJobRecord timerJobRecord;
        Date startTime = new Date();

        JobKey jobKey = context.getTrigger().getJobKey();
        TimerJob timerJob = new TimerJob();
        timerJob.setJobName(jobKey.getName());
        timerJob.setJobGroup(jobKey.getGroup());
        TimerJob currentJob = timerJobBiz.selectOne(timerJob);
        JSONObject jsonObject = null;
        try {
            jsonObject = NeteaseUtil.songRank(currentJob.getTargetUserid());
        }catch (PermissionDeniedException e){
            log.info("{} {} 停止任务",currentJob.getTargetNickname(),currentJob.getTargetUserid());
            baseQuartzBiz.deleteScheduleJob(currentJob.getJobName(),currentJob.getJobGroup());
            currentJob.setStatus(TimerJob.STATUS_STOP);
            currentJob.setUpdTime(new Date());
            timerJobBiz.updateById(currentJob);
            return;
        }

        if(jsonObject == null){
            return;
        }
        List<SongRankData> songRankDataList = new ArrayList<SongRankData>(100);

        JSONArray array = jsonObject.getJSONArray("weekData");
        // job record id
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());
        //暂无最近一周听歌数据
        if(array.size() == 0){
            log.debug("{} {} 执行结束,无周榜数据",currentJob.getTargetNickname(),currentJob.getTargetUserid());
            // 无周榜数据 若没有记录新数据 同样插入一条快照记录 表示初始数据
            if(oldRecord == null){
                log.info("{} {} 初始空白数据",currentJob.getTargetNickname(),currentJob.getTargetUserid());
                timerJobRecord = TimerJobRecord.builder()
                        .startTime(startTime)
                        .snapshot("-1")
                        .newData(1)
                        .id(uuid)
                        .count(0)
                        .jobId(currentJob.getId())
                        .endTime(new Date()).build();
                timerJobRecordBiz.insert(timerJobRecord);
            }
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<array.size();i++) {
            SongRankDataDTO songRankDataDTO = jsonArrayItemToDto(i,array.get(i));
            songRankDataList.add(new SongRankData(
                    uuid,songRankDataDTO
            ));
            sb.append(songRankDataDTO.toString());

        }
        String snapshot = sb.toString().hashCode()+"";

        int newData = 0;
        //数据变更
        if(oldRecord == null||!snapshot.equals(oldRecord.getSnapshot())){
            newData = 1;
            if (oldRecord != null) {
                SongRankDataDiff firstDiff = recordDiffData(getOldDataList(oldRecord.getId()),songRankDataList,uuid,currentJob.getTargetUserid());
                //不是批量更新，判断是否推送模板消息
                if(firstDiff != null && firstDiff.getIsBatchUpdate() == 0){
                    try{
                        Example example = new Example(TemplateMessage.class);
                        example.createCriteria().andEqualTo("targetUserId",currentJob.getTargetUserid()).andEqualTo("isValid",1);
                        List<TemplateMessage> msg = templateMessageBiz.selectByExample(example);
                        templateMessageBiz.pushTemplateMsg(msg,firstDiff);
                    }catch (Exception e){
                        log.error("{} {} 推送模板消息时出现异常: {}",currentJob.getTargetNickname(),currentJob.getTargetUserid(),e.getMessage());
                    }
                }
                log.info("{} {} 数据变更",currentJob.getTargetNickname(),currentJob.getTargetUserid());
            }else{
                log.info("{} {} 初始数据",currentJob.getTargetNickname(),currentJob.getTargetUserid());
            }
            //记录整榜数据
            if(songRankDataList.size()>0){
                songRankDataBiz.insertByBatch(songRankDataList);
                //插入当前整榜后，删除旧数据
                if(oldRecord!=null){
                    songRankDataBiz.deleteByRecordId(oldRecord.getId());
                }
            }else{
                log.warn("{} {} 获取 songRankDataList size 为 0",currentJob.getTargetNickname(),currentJob.getTargetUserid());
                newData = 0;
            }

            timerJobRecord = TimerJobRecord.builder()
                    .startTime(startTime).newData(newData)
                    .snapshot(snapshot)
                    .id(uuid)
                    .count(0)
                    .jobId(currentJob.getId())
                    .endTime(new Date()).build();
            timerJobRecordBiz.insert(timerJobRecord);
        }

    }

    /**
     * 根据记录id查询详细听歌榜数据
     * @param oldJobRecordId 记录id
     * @return 听歌排行榜数据
     */
    private List<SongRankData> getOldDataList(String oldJobRecordId){
        Example example = new Example(SongRankData.class);
        example.createCriteria().andEqualTo("jobRecordId",oldJobRecordId);
        return songRankDataBiz.selectByExample(example);
    }

    /**
     * 比较记录变化数据
     * @param oldDataList
     * @param newDataList
     * @param jobRecordId
     * @param targetUserId
     * @return
     */
    private  SongRankDataDiff recordDiffData(List<SongRankData> oldDataList, List<SongRankData> newDataList,String jobRecordId ,String targetUserId){
        Map<String,SongRankData> oldMap = new HashMap<String, SongRankData>(oldDataList.size());
        Map<String,SongRankData> newMap = new HashMap<String, SongRankData>(newDataList.size());
        for(SongRankData songRankData : oldDataList){
            oldMap.put(songRankData.getSong(),songRankData);
        }
        for(SongRankData songRankData : newDataList){
            newMap.put(songRankData.getSong(),songRankData);
        }
        //待插入list
        List<SongRankDataDiff> addList = new ArrayList<>();
        Iterator<Map.Entry<String, SongRankData>> entries = newMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, SongRankData> entry = entries.next();
            //新增歌曲
            if(oldMap.get(entry.getKey())==null){
                SongRankDataDiff songRankDataDiff = SongRankDataDiff.builder()
                        .jobRecordId(jobRecordId)
                        .targetUserId(targetUserId)
                        .rankChange(-1)
                        .song(entry.getKey())
                        .singer(entry.getValue().getSinger())
                        .build();
                addList.add(songRankDataDiff);
            }
            //旧榜存在 排行不同 且新榜大于旧榜排序
            else{
                Integer newRank = entry.getValue().getRank();
                Integer oldRank = oldMap.get(entry.getKey()).getRank();
                if(newRank<oldRank){
                    SongRankDataDiff songRankDataDiff = SongRankDataDiff.builder()
                            .jobRecordId(jobRecordId)
                            .targetUserId(targetUserId)
                            .rankChange(oldRank-newRank)
                            .song(entry.getKey())
                            .singer(entry.getValue().getSinger())
                            .build();
                    addList.add(songRankDataDiff);
                }
            }
        }
        int isBatchUpdate = 0;
        // 如果同一时间抓到的歌曲变化数大于三，判定为系统自动批量更新数据
        if(addList.size()>3){
            isBatchUpdate = 1;
        }
        //如果时间为3~5点，判定为系统更新数据
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(3 == hour||4 == hour||5 == hour){
            //日期减一天
            c.add(Calendar.DAY_OF_MONTH,-1);
            isBatchUpdate = 1;
        }

        if(addList.size()==0){
                log.warn("快照不同，但未找出变动歌曲");
                log.debug(JSONObject.toJSONString(oldDataList));
                log.debug(JSONObject.toJSONString(newDataList));
                return null;
        }
        for(SongRankDataDiff rankDataDiff : addList){
            rankDataDiff.setIsBatchUpdate(isBatchUpdate);
            rankDataDiff.setChangeTime(c.getTime());
            songRankDataDiffBiz.insert(rankDataDiff);
        }
        SongRankDataDiff firstData = addList.get(0);
        firstData.setIsBatchUpdate(isBatchUpdate);
        return firstData;
    }

    private SongRankDataDTO jsonArrayItemToDto(Integer index,Object arrayItem){
        String ratio = JSONObject.parseObject(arrayItem.toString()).get("score").toString();
        Object songObj = JSONObject.parseObject(arrayItem.toString()).get("song");
        JSONArray ar = JSONObject.parseObject(songObj.toString()).getJSONArray("ar");
        JSONObject arObj = JSONObject.parseObject(ar.get(0).toString());
        String singerName = arObj.get("name").toString();
        String songName = JSONObject.parseObject(songObj.toString()).get("name").toString();
        return new SongRankDataDTO(songName,singerName,index+1,ratio);
    }
}
