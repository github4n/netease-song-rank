package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.TimerJobRecord;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
* @author zhaohw
* @date 2018/1/10 17:27
*/
public interface TimerJobRecordMapper extends Mapper<TimerJobRecord>{

    /**
     * 获取最近一次执行记录
     * @param jobId
     * @return
     */
    TimerJobRecord getLatestRecord(@Param("jobId") Integer jobId);
}
