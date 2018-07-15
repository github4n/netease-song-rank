package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.mapper.TimerJobMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-01-10 17:44
 */
@Service
public class TimerJobBiz extends BaseBiz<TimerJobMapper,TimerJob>{

    /**
     * 通过网易云用户id查询任务
     * @param userId
     * @return
     */
    public TimerJob findByTargetUserId(String userId){
        Example example = new Example(TimerJob.class);
        example.createCriteria().andEqualTo("targetUserid",userId);
        List<TimerJob> list = mapper.selectByExample(example);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
