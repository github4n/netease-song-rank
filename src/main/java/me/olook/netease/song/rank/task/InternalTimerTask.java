package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.biz.ProxyPoolBiz;
import me.olook.netease.song.rank.biz.TemplateMessageBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.util.proxy.ProxyPool;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-16 10:03
 */
@Slf4j
@Component
@EnableAsync
public class InternalTimerTask {

    @Resource
    private TimerJobBiz timerJobBiz;

    @Resource
    private TemplateMessageBiz templateMessageBiz;

    @Resource
    private ProxyPoolBiz proxyPoolBiz;

    @Async
    @Scheduled(initialDelay = 1000*5 , fixedDelay = 1000 * 60 * 5 )
    public void fixProxyPool() {
        log.info("fix proxy pool , active size {}" , ProxyPool.activeSize());
        proxyPoolBiz.fixProxyPool();
    }

    @Scheduled(initialDelay = 1000*15 , fixedDelay = 1000 * 60 * 60 * 24 * 2)
    public void cleanTimerJob() {
        int num = timerJobBiz.updateExpiredTimerJobs();
        log.info("expired timer jobs clean : [{}] ",num);
    }

    @Scheduled(initialDelay = 1000*20 , fixedDelay = 1000 * 60 * 60 * 24 * 2)
    public void cleanPushTemplate() {
        int num = templateMessageBiz.updateExpiredTemplates();
        log.info("expired push templates clean : [{}] ",num);
    }

}
