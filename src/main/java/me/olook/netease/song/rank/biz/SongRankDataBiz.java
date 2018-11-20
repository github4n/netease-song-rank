package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.repository.SongRankDataRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-20 16:48
 */
@Service
public class SongRankDataBiz {

    @Resource
    private SongRankDataRepository songRankDataRepository;

    public List<SongRankData> getOldDataList(String oldJobRecordId){
        return songRankDataRepository.findByJobRecordIdOrderByRankAsc(oldJobRecordId);
    }

    public SongRankData save(SongRankData songRankData){
        return songRankDataRepository.save(songRankData);
    }

    public void deleteLastSongRankData(String oldJobRecordId){
        songRankDataRepository.deleteByJobRecordId(oldJobRecordId);
    }
}
