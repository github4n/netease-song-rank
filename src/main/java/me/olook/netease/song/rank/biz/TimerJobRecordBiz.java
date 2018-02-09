package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.TimerJobRecord;
import me.olook.netease.song.rank.mapper.TimerJobRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhaohw
 * @date 2018-01-10 17:45
 */
@Service
public class TimerJobRecordBiz extends BaseBiz<TimerJobRecordMapper,TimerJobRecord>{

    /**
     * 查找最近一次改任务数据变更记录
     * @param jobId
     * @return
     */
    public TimerJobRecord getLatestRecord(Integer jobId){
        return mapper.getLatestRecord(jobId);
    };
}
