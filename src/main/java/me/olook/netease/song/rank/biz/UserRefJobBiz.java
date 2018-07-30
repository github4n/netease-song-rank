package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.mapper.UserRefJobMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-05 20:46
 */
@Service
public class UserRefJobBiz extends BaseBiz<UserRefJobMapper,UserRefJob> {

    /**
     * 通过openid查询用户关联的任务
     * @param openid
     * @return
     */
    public List<UserRefJob> getUserJobByOpenId(String openid){
        Example example = new Example(UserRefJob.class);
        example.createCriteria().andEqualTo("openId",openid).andEqualTo("delFlag",0);
        return this.selectByExample(example);
    }

    /**
     * 通过openid和targetUserId查询单个任务
     * @param targetUserId
     * @return
     */
    public UserRefJob getUserJobByTargetUserId(String targetUserId){
        Example example = new Example(UserRefJob.class);
        example.createCriteria().andEqualTo("targetUserId",targetUserId).andEqualTo("delFlag",0);
        //todo 任务已删除 推送错误
        return this.selectByExample(example).get(0);
    }

    /**
     * 通过网易云id逻辑删除任务绑定
     */
    public void updateDelFlagByTargetUserId(String targetUserId,Integer delFlag){
        Example example = new Example(UserRefJob.class);
        example.createCriteria().andEqualTo("targetUserId",targetUserId);
        UserRefJob userRefJob = new UserRefJob();
        userRefJob.setTargetUserId(targetUserId);
        userRefJob.setDelFlag(delFlag);
        userRefJob.setUpdTime(new Date());
        mapper.updateByExampleSelective(userRefJob,example);
    }
}
