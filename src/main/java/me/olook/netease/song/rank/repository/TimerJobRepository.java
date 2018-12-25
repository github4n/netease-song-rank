package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.TimerJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-08 17:39
 */
@Repository
public interface TimerJobRepository extends JpaRepository<TimerJob,Integer> {

    /**
     * 根据targetUserId查找爬虫任务
     * 一个targetUserId最多应该只存在一个任务
     */
    TimerJob findByTargetUserId(String targetUserId);

    /**
     * 查询不活跃任务
     * @param status 任务状态
     * @param date 判定过期时间点
     * @return timer job
     */
    List<TimerJob> findTimerJobsByStatusAndUpdTimeBefore(Integer status, Date date);

    /**
     * 通过分组及任务状态查找任务
     * @param jobGroup 分组
     * @param status 任务状态
     * @return 任务列表
     */
    List<TimerJob> findByJobGroupAndStatusOrderByUpdTimeDesc(String jobGroup,Integer status);
}
