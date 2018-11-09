package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.WxUser;
import me.olook.netease.song.rank.repository.WxUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-09 10:38
 */
@Service
public class WxUserBiz {

    @Resource
    private WxUserRepository wxUserRepository;

    public WxUser save(WxUser wxUser){
        return wxUserRepository.save(wxUser);
    }

    public WxUser findByOpenId(String openid){
        return wxUserRepository.findByOpenId(openid);
    }

}
