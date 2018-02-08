package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.SongRankData;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
* @author zhaohw
* @date 2018/2/8 15:07
*/
public interface SongRankDataMapper extends Mapper<SongRankData> {
    /**
     * 批量插入数据
     * @param songRankDataList
     * @return
     */
    int insertByBatch(List<SongRankData> songRankDataList);
}
