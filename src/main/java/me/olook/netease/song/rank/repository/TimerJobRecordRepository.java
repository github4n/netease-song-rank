package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.TimerJobRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhaohw
 * @date 2018-11-19 17:50
 */
@Repository
public interface TimerJobRecordRepository extends JpaRepository<TimerJobRecord,Integer> {

    TimerJobRecord findFirstByJobIdAndNewDataOrderByEndTimeDesc(Integer jobId,Integer newData);
}
