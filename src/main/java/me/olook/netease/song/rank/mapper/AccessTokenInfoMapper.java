package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.AccessTokenInfo;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhaohw
 * @date 2018-03-08 13:37
 */
public interface AccessTokenInfoMapper extends Mapper<AccessTokenInfo>{

    /**
     * 获取最新可用的token
     * @return
     */
    AccessTokenInfo getLatestValidToken();
}
