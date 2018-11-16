package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhaohw
 * @date 2018-11-16 10:03
 */
@Slf4j
@Component
@EnableAsync
public class InternalCleanTask {

    @Async
    @Scheduled(initialDelay = 1000*10 , fixedDelay = 1000 * 10)
    public void cleanTimerJob() {
        log.info("clean timer job");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("clean timer job end");
    }

    @Async
    @Scheduled(initialDelay = 1000*10 , fixedDelay = 1000 * 10)
    public void cleanPushTemplate() {
        log.info("clean push template");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("clean push template end");
    }
}
