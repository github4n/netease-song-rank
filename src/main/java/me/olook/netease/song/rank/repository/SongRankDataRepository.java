package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.SongRankData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRankDataRepository extends JpaRepository<SongRankData,Integer> {

    List<SongRankData> findByJobRecordIdOrderByRankAsc(String jobRecordId);

    void deleteByJobRecordId(String jobRecordId);
}
