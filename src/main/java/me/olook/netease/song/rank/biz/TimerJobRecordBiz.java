package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.TimerJobRecord;
import me.olook.netease.song.rank.repository.TimerJobRecordRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-19 17:49
 */
@Service
public class TimerJobRecordBiz {

    @Resource
    private TimerJobRecordRepository timerJobRecordRepository;

    public TimerJobRecord getLatestRecord(Integer jobId){
        return timerJobRecordRepository.findByJobIdAndNewDataOrderByEndTimeDesc(jobId,1);
    }

    public TimerJobRecord save(TimerJobRecord timerJobRecord){
        return timerJobRecordRepository.save(timerJobRecord);
    }
}
