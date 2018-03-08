package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.AccessTokenInfo;
import me.olook.netease.song.rank.mapper.AccessTokenInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhaohw
 * @date 2018-03-08 13:38
 */
@Service
public class AccessTokenInfoBiz extends BaseBiz<AccessTokenInfoMapper,AccessTokenInfo>{

    /**
     *  获取最新可用的token
     * @return
     */
    public AccessTokenInfo getLatestValidToken(){
        return mapper.getLatestValidToken();
    }
}
