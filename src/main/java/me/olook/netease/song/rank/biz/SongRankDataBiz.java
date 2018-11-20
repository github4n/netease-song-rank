package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.repository.SongRankDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteByJobRecordId(String oldJobRecordId){
        if(oldJobRecordId!=null) {
            songRankDataRepository.deleteByJobRecordId(oldJobRecordId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAndDeleteSongRankData(List<SongRankData> songRankDataList, String oldJobRecordId){
        saveSongRankData(songRankDataList);
        deleteByJobRecordId(oldJobRecordId);
    }

    private void saveSongRankData(List<SongRankData> songRankDataList){
        songRankDataList.forEach(songRankDataRepository::save);
    }

}
