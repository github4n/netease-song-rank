package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.mapper.SongRankDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-02-08 15:06
 */
@Service
public class SongRankDataBiz extends BaseBiz<SongRankDataMapper,SongRankData>{

    public int insertByBatch(List<SongRankData> songRankDataList){
        return mapper.insertByBatch(songRankDataList);
    }
}
