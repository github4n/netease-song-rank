package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.SongRankDataDiff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author zhaohw
 * @date 2018-11-13 14:44
 */
@Repository
public interface SongRankDataDiffRepository extends JpaRepository<SongRankDataDiff,Integer> {

    Page<SongRankDataDiff> findByTargetUserIdOrderByChangeTimeDesc(String userId,Pageable pageable);

    /**
     * 获取某组userId的最新听歌数据
     * @param targetUserIds userId集合
     * @param pageable 分页数据
     * @return 听歌变化数据
     */
    Page<SongRankDataDiff> findByTargetUserIdInOrderByChangeTimeDesc(Collection targetUserIds,Pageable pageable);
}
