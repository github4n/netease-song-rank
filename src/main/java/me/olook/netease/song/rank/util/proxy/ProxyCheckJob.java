package me.olook.netease.song.rank.util.proxy;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;

import java.util.concurrent.Callable;

/**
 * @author zhaohw
 */
@Slf4j
public class ProxyCheckJob implements Callable {

    private ProxyInfo proxyInfo;

    public ProxyCheckJob(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    @Override
    public Object call() {
        boolean b = NetEaseHttpClient.checkProxy(proxyInfo.getIp(), proxyInfo.getPort());
        if(b){
            ProxyPool.offer(proxyInfo);
            log.info("accept proxy {}",proxyInfo);
        }else{
            log.warn("discard proxy {}",proxyInfo);
        }
        return b;
    }

}
