package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.TimerJobRecord;
import me.olook.netease.song.rank.repository.TimerJobRecordRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhaohw
 * @date 2018-11-19 17:49
 */
@Service
public class TimerJobRecordBiz {

    @Resource
    private TimerJobRecordRepository timerJobRecordRepository;

    /**
     * 获取最近一次该任务有数据变更的记录
     */
    public TimerJobRecord getLatestRecord(Integer jobId){
        return timerJobRecordRepository.findFirstByJobIdAndNewDataOrderByEndTimeDesc(jobId,1);
    }

    public TimerJobRecord save(TimerJobRecord timerJobRecord){
        return timerJobRecordRepository.save(timerJobRecord);
    }

    public void saveTimerJobRecord(TimerJob currentJob,String uuid){
        this.saveTimerJobRecord(currentJob,uuid,"-1");
    }

    public void saveTimerJobRecord(TimerJob currentJob, String jobRecordId, String snapshot){
        TimerJobRecord timerJobRecord = TimerJobRecord.builder()
                .startTime(new Date())
                .snapshot(snapshot)
                .newData(1)
                .id(jobRecordId)
                .count(0)
                .jobId(currentJob.getId())
                .endTime(new Date()).build();
        timerJobRecordRepository.save(timerJobRecord);
    }
}
