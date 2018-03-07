package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.mapper.SongRankDataDiffMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-02-08 18:20
 */
@Service
public class SongRankDataDiffBiz extends BaseBiz<SongRankDataDiffMapper,SongRankDataDiff>{

    /**
     * 查出最近的n条数据
     * @param userId
     * @param limit
     * @return
     */
    public List<SongRankDataDiff> getLatestRecordLimit(String userId, Integer limit){
        return mapper.getLatestRecordLimit(userId,limit);
    }

}
