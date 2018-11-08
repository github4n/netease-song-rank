package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.AccessTokenInfo;
import me.olook.netease.song.rank.repository.AccessTokenInfoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-08 17:01
 */
@Service
public class AccessTokenInfoBiz {

    @Resource
    private AccessTokenInfoRepository repository;

    public AccessTokenInfo getLatestValidToken(){
        return repository.findFirstByIsValidOrderByCrtTimeDesc(1);
    }
}
