package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.SongRankDataDiff;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
* @author zhaohw
* @date 2018/2/8 18:20
*/
public interface SongRankDataDiffMapper extends Mapper<SongRankDataDiff>{

    /**
     * 查出最近的n条数据
     * @param userId
     * @param limit
     * @return
     */
    List<SongRankDataDiff> getLatestRecordLimit(@Param("userId") String userId,@Param("limit") Integer limit);
}
