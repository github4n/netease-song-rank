package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.TimerJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhaohw
 * @date 2018-11-08 17:39
 */
@Repository
public interface TimerJobRepository extends JpaRepository<TimerJob,Integer> {

    TimerJob findByTargetUserId(String targetUserId);
}
