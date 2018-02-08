package me.olook.netease.song.rank;

import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化定时任务
 * @author zhaohw
 * @date 2018-01-25 10:00
 */
@Component
public class QuartzStartup implements ApplicationListener<ApplicationReadyEvent> {

    private Logger log = LoggerFactory.getLogger(QuartzStartup.class);

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            log.info("QuartzStartup onApplicationEvent start!");
            List<TimerJob> jobList = timerJobBiz.selectListAll();
            for (TimerJob timerJob : jobList) {
                if (timerJob.getStatus() == 1) {
                    baseQuartzBiz.createJobByGosTimerJob(timerJob);
                }
            }
    }
}
