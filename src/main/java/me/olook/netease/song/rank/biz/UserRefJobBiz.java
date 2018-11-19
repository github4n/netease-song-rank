package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.repository.UserRefJobRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 10:43
 */
@Service
public class UserRefJobBiz {

    @Resource
    private UserRefJobRepository userRefJobRepository;

    public List<UserRefJob> findByTargetUserIdAndDelFlag(String targetUserId, Integer delFlag){
        return userRefJobRepository.findByTargetUserIdAndDelFlag(targetUserId,delFlag);
    }

    public List<UserRefJob> findByOpenIdAndDelFlag(String openid,Integer delFlag){
        return userRefJobRepository.findByOpenIdAndDelFlag(openid,delFlag);
    }

    public UserRefJob save(UserRefJob userRefJob){
        return userRefJobRepository.save(userRefJob);
    }
}
