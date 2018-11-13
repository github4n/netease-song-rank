package me.olook.netease.song.rank.config;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaohw
 * @date 2018-11-13 15:23
 */
@Slf4j
@Component
public class TimerJobConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private final TimerJobBiz timerJobBiz;

    private final BaseQuartzBiz baseQuartzBiz;

    @Autowired
    public TimerJobConfiguration(TimerJobBiz timerJobBiz, BaseQuartzBiz baseQuartzBiz) {
        this.timerJobBiz = timerJobBiz;
        this.baseQuartzBiz = baseQuartzBiz;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("QuartzStartup onApplicationEvent start!");
        AtomicInteger count = new AtomicInteger();
        timerJobBiz.findAll().stream()
                .filter(p->p.getStatus().equals(TimerJob.STATUS_RUN))
                .peek(p->{
                    count.getAndIncrement();
                })
                .forEach(baseQuartzBiz::createTimerJob);
        log.info("[{}] timer job has been created.",count);
    }
}
