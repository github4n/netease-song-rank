package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.TimerJob;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
* @定时任务mapper
* @author zhaohw
* @date 2018/1/10 17:25
*/
public interface TimerJobMapper extends Mapper<TimerJob> {

    List<TimerJob> findExpiredTimerJob(@Param("dayDiff") Integer dayDiff);
}
