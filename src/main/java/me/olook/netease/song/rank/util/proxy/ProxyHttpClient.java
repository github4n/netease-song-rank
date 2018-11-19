package me.olook.netease.song.rank.util.proxy;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.properties.ProxyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-19 14:21
 */
@Slf4j
@Component
@EnableConfigurationProperties(ProxyProperties.class)
public class ProxyHttpClient {

    @Resource
    private ProxyProperties proxyProperties;

    @Resource
    private RestTemplate restTemplate;

    public String getProxy(){
        String url = ProxyApiUrl.DA_XIANG+"?tid="+proxyProperties.getOrderId()+proxyProperties.getParams();
        String proxyStr = restTemplate.getForEntity(url, String.class).getBody();
        log.info("get proxy info : {}",proxyStr);
        return proxyStr;
    }
}
