package me.olook.netease.song.rank.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.TimerJobRecordBiz;
import me.olook.netease.song.rank.dto.SongRankDataDTO;
import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.TimerJobRecord;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyPoolUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zhaohw
 * @date 2018-11-13 15:19
 */
@Slf4j
public class RecordRankTask implements Job {

    @Resource
    private TimerJobBiz timerJobBiz;

    @Resource
    private TimerJobRecordBiz timerJobRecordBiz;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Date startTime = new Date();
        JobKey jobKey = context.getTrigger().getJobKey();
        TimerJob currentJob = timerJobBiz.findByTargetUserId(jobKey.getName());

        ProxyInfo proxyInfo = ProxyPoolUtil.get();
        TimerJobRecord timerJobRecord;
        JSONObject jsonObject =
                NetEaseHttpClient.getSongRankData(currentJob.getTargetUserId(),proxyInfo );
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // todo   数据解析流程   zhaohw 2018/11/19 18:04
        if(jsonObject != null){
            List<SongRankData> songRankDataList = new ArrayList<SongRankData>(100);
            JSONArray array = jsonObject.getJSONArray("weekData");
            TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());
            if(array.size() == 0){
                log.debug("{} {} 执行结束,无周榜数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
                // 无周榜数据 若没有记录新数据 同样插入一条快照记录 表示初始数据
                if(oldRecord == null){
                    log.info("{} {} 初始空白数据",currentJob.getTargetNickname(),currentJob.getTargetUserId());
                    timerJobRecord = TimerJobRecord.builder()
                            .startTime(startTime)
                            .snapshot("-1")
                            .newData(1)
                            .id(uuid)
                            .count(0)
                            .jobId(currentJob.getId())
                            .endTime(new Date()).build();
                    timerJobRecordBiz.save(timerJobRecord);
                }
            }
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<array.size();i++) {
                SongRankDataDTO songRankDataDTO = jsonArrayItemToDto(i,array.get(i));
                sb.append(songRankDataDTO.toString());

            }
            String snapshot = sb.toString().hashCode()+"";
        }else {
            ProxyPoolUtil.fail(proxyInfo);
        }
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
