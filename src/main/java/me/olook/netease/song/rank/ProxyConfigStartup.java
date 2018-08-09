package me.olook.netease.song.rank;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.proxy.ProxyProperties;
import me.olook.netease.song.rank.util.proxy.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zhaohw
 * @date 2018-08-09 10:35
 */
@Slf4j
@Component
public class ProxyConfigStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ProxyProperties properties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("init proxy pool config ...");

        ProxyUtil.PROXY_ORDER_ID = properties.getOrderId();
        log.info("init proxy pool order id : {}", ProxyUtil.PROXY_ORDER_ID);

        ProxyUtil.PROXY_API_OPTION = properties.getOption().replace("\\","");
        log.info("init proxy pool api option : {}", ProxyUtil.PROXY_API_OPTION);

        ProxyUtil.DEFAULT_PROXY_POOL_SIZE = properties.getPoolSize();
        log.info("init proxy pool size : {}", ProxyUtil.DEFAULT_PROXY_POOL_SIZE);
    }
}
