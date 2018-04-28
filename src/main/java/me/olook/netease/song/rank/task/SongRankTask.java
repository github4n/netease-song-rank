package me.olook.netease.song.rank.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.biz.*;
import me.olook.netease.song.rank.entity.*;
import me.olook.netease.song.rank.util.NeteaseUtil;
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
    private SongRankDataBiz songRankDataBiz;

    @Autowired
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @Autowired
    private TimerJobRecordBiz timerJobRecordBiz;

    @Autowired
    private TemplateMessageBiz templateMessageBiz;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TimerJobRecord timerJobRecord = new TimerJobRecord();
        timerJobRecord.setStartTime(new Date());
        JobKey jobKey = context.getTrigger().getJobKey();
        TimerJob timerJob = new TimerJob();
        timerJob.setJobName(jobKey.getName());
        timerJob.setJobGroup(jobKey.getGroup());
        //log.info("job name :" + jobKey.getName() + " job group :" +jobKey.getGroup() + " execute");
        TimerJob currentJob = timerJobBiz.selectOne(timerJob);

        JSONObject jsonObject = NeteaseUtil.songRank(currentJob.getTargetUserid());
        if(jsonObject==null){
            return;
        }
        List<SongRankData> songRankDataList = new ArrayList<SongRankData>(100);

        JSONArray array = jsonObject.getJSONArray("weekData");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //暂无最近一周听歌数据
        if(array.size()==0){
//            timerJobRecord.setSnapshot("-1");
//            timerJobRecord.setNewData(0);
//            timerJobRecord.setId(uuid);
//            timerJobRecord.setCount(0);
//            timerJobRecord.setJobId(currentJob.getId());
//            timerJobRecord.setEndTime(new Date());
//            timerJobRecordBiz.insert(timerJobRecord);
            log.info("{} 执行结束,无周榜数据",currentJob.getJobName());
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<array.size();i++) {
            String ratio = JSONObject.parseObject(array.get(i).toString()).get("score").toString();
            Object songObj = JSONObject.parseObject(array.get(i).toString()).get("song");
            JSONArray ar = JSONObject.parseObject(songObj.toString()).getJSONArray("ar");
            JSONObject arObj = JSONObject.parseObject(ar.get(0).toString());
            String singerName = arObj.get("name").toString();
            String songName = JSONObject.parseObject(songObj.toString()).get("name").toString();
            songRankDataList.add(new SongRankData(
                    uuid,
                    i+1,
                    songName,
                    singerName,
                    ratio
            ));
            sb.append(songName).append(singerName).append(i).append(ratio);

        }
        String snapshot = sb.toString().hashCode()+"";
        TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());
        timerJobRecord.setNewData(0);
        //数据变更
        if(oldRecord==null||!snapshot.equals(oldRecord.getSnapshot())){
            timerJobRecord.setNewData(1);
            if (oldRecord != null) {
                SongRankDataDiff firstDiff = recordDiffData(getOldDataList(oldRecord.getId()),songRankDataList,uuid,currentJob.getTargetUserid());
                //不是批量更新，判断是否推送模板消息
                if(firstDiff!=null&&firstDiff.getIsBatchUpdate()==0){
                    try{
                        Example example = new Example(TemplateMessage.class);
                        example.createCriteria().andEqualTo("targetUserId",currentJob.getTargetUserid()).andEqualTo("isValid",1);
                        List<TemplateMessage> msg = templateMessageBiz.selectByExample(example);
                        templateMessageBiz.pushTemplateMsg(msg,firstDiff);
                    }catch (Exception e){
                        log.error("推送模板消息时出现异常: {}",e.getMessage());
                    }
                }
                log.info("{} 数据变更",currentJob.getJobName());
            }else{
                log.info("{} 初始数据",currentJob.getJobName());
            }
            //记录整榜数据
            if(songRankDataList.size()>0){
                songRankDataBiz.insertByBatch(songRankDataList);
                //插入当前整榜后，删除旧数据
                if(oldRecord!=null){
                    songRankDataBiz.deleteByRecordId(oldRecord.getId());
                }
            }else{
                log.warn("{} 获取 songRankDataList size 为 0",currentJob.getJobName());
                timerJobRecord.setNewData(0);
            }

            timerJobRecord.setSnapshot(snapshot);
            timerJobRecord.setId(uuid);
            timerJobRecord.setCount(0);
            timerJobRecord.setJobId(currentJob.getId());
            timerJobRecord.setEndTime(new Date());
            timerJobRecordBiz.insert(timerJobRecord);
        }

        //log.info(currentJob.getJobName()+" 执行结束");

    }


    private List<SongRankData> getOldDataList(String oldJobRecordId){
        Example example = new Example(SongRankData.class);
        example.createCriteria().andEqualTo("jobRecordId",oldJobRecordId);
        return songRankDataBiz.selectByExample(example);
    }

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
                SongRankDataDiff songRankDataDiff = new SongRankDataDiff();
                songRankDataDiff.setJobRecordId(jobRecordId);
                songRankDataDiff.setTargetUserId(targetUserId);
                songRankDataDiff.setRankChange(-1);
                songRankDataDiff.setSong(entry.getKey());
                songRankDataDiff.setSinger(entry.getValue().getSinger());
                //songRankDataDiff.setChangeTime(new Date());
                addList.add(songRankDataDiff);
            }
            //旧榜存在 排行不同 且新榜大于旧榜排序
            else{
                Integer newRank = entry.getValue().getRank();
                Integer oldRank = oldMap.get(entry.getKey()).getRank();
                if(newRank<oldRank){
                    SongRankDataDiff songRankDataDiff = new SongRankDataDiff();
                    songRankDataDiff.setJobRecordId(jobRecordId);
                    songRankDataDiff.setTargetUserId(targetUserId);
                    songRankDataDiff.setRankChange(oldRank-newRank);
                    songRankDataDiff.setSong(entry.getKey());
                    songRankDataDiff.setSinger(entry.getValue().getSinger());
                    //songRankDataDiff.setChangeTime(new Date());
                    addList.add(songRankDataDiff);
                }
            }
        }
        Integer isBatchUpdate = 0;
//        //如果同一时间抓到的歌曲变化数大于三，判定为系统自动批量更新数据
        if(addList.size()>3){
            isBatchUpdate = 1;
        }
        //如果时间为3~5点，判定为系统更新数据
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(3==hour||4==hour||5==hour){
            //日期减一天
            c.add(Calendar.DAY_OF_MONTH,-1);
            isBatchUpdate = 1;
        }

        if(addList.size()==0){
                log.warn("快照不同，但未找出变动歌曲!");
                log.warn(JSONObject.toJSONString(oldDataList));
                log.warn(JSONObject.toJSONString(newDataList));
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

}
