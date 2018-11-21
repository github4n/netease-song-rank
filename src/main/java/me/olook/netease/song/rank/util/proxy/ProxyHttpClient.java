package me.olook.netease.song.rank.util.proxy;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.properties.ProxyProperties;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
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

    public void fixProxyPool(){
        while(ProxyPoolUtil.activeSize() < proxyProperties.getPoolSize()){
            resolve(getProxy());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getProxy(){
        String url = ProxyApiUrl.DA_XIANG
                +"?tid="+proxyProperties.getOrderId()
                +proxyProperties.getParams();
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    private void resolve(String multiProxyStr){
        //一次返回多个代理ip信息处理
        String[] proxies = multiProxyStr.split(System.getProperty("line.separator"));
        //单个解析
        for(String proxyStr : proxies){
            if(proxyStr.split(":").length < 2){
                log.error(proxyStr);
            }else {
                String ip = proxyStr.split(":")[0];
                Integer port = Integer.parseInt(proxyStr.split(":")[1]);
                ProxyInfo proxyInfo = new ProxyInfo(ip,port);
                boolean isValid = NetEaseHttpClient.checkProxy(ip,port);
                if(isValid){
                    log.info("accept proxy {}:{}",ip,port);
                    ProxyPoolUtil.workQueue.add(proxyInfo);
                }else{
                    log.warn("discard proxy {}:{}",ip,port);
                }
            }
        }
    }

}
