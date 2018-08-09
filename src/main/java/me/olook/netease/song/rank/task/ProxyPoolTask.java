package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.proxy.ProxyUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static me.olook.netease.song.rank.util.proxy.ProxyUtil.DEFAULT_PROXY_POOL_SIZE;
import static me.olook.netease.song.rank.util.proxy.ProxyUtil.currentProxy;

/**
 * @author zhaohw
 * @date 2018-08-09 9:37
 */
@Slf4j
@Component
public class ProxyPoolTask {

    @Scheduled(fixedDelay = 1000 * 10)
    public void fix() {
        int size = currentProxy.size();
        log.info("当前存活有效代理数量: {}",currentProxy.size());
        for(int i = 0; i< DEFAULT_PROXY_POOL_SIZE ; i++) {
            if(currentProxy.get(i) == null){
                ProxyUtil.fixProxyPool(i);
            }
        }
    }
}
