package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.repository.TimerJobRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 9:49
 */
@Service
public class TimerJobBiz {

    @Resource
    private TimerJobRepository timerJobRepository;

    public TimerJob findByTargetUserId(String userId){
        return timerJobRepository.findByTargetUserId(userId);
    }

    public TimerJob save(TimerJob timerJob){
        return timerJobRepository.save(timerJob);
    }

    public List<TimerJob> findAll(){
        return timerJobRepository.findAll();
    }
}
