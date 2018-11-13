package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.repository.SongRankDataDiffRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-13 14:52
 */
@Service
public class SongRankDataDiffBiz {

    @Resource
    private SongRankDataDiffRepository diffRepository;

    public List<SongRankDataDiff> findLatestRecordRank(String userId){
        Pageable pageable = PageRequest.of(0,10);
        Page<SongRankDataDiff> recordRanks =
                diffRepository.findByTargetUserIdOrderByChangeTimeDesc(userId, pageable);
        return recordRanks.getContent();
    }
}
