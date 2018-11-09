package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.repository.TimerJobRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-09 9:49
 */
@Service
public class TimerJobBiz {

    @Resource
    private TimerJobRepository timerJobRepository;
}
