package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.mapper.UserRefJobMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
        example.createCriteria().andEqualTo("openId",openid);
        return this.selectByExample(example);
    }
}
