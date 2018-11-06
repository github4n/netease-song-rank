package me.olook.netease.song.rank;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
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
@Slf4j
@Component
public class QuartzStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            log.info("QuartzStartup onApplicationEvent start!");
            List<TimerJob> jobList = timerJobBiz.selectListAll();
            jobList.stream()
                    .filter(p->TimerJob.STATUS_RUN.equals(p.getStatus()))
                    .forEach(baseQuartzBiz::createJobByGosTimerJob);
    }

    private void disperseTimerJob(){
        List<TimerJob> jobList = timerJobBiz.selectListAll();
        log.info("rebuild quartz expression ...");
            jobList.stream()
                    .filter(p-> "group1".equals(p.getJobGroup()))
                    .forEach(p->{
                int second = (int) (Math.random() * 59);
                String expression = second+" */1 * * * ?";
                p.setCronExpression(expression);
                timerJobBiz.updateSelectiveById(p);
            });
    }
}
