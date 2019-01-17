package me.olook.netease.song.rank.util.proxy;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;

/**
 * @author zhaohw
 */
@Slf4j
public class ProxyCheckJob implements Runnable{

    private ProxyInfo proxyInfo;

    public ProxyCheckJob(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    @Override
    public void run() {
        boolean b = NetEaseHttpClient.checkProxy(proxyInfo.getIp(), proxyInfo.getPort());
        if(b){
            ProxyPool.offer(proxyInfo);
            log.info("accept proxy {}",proxyInfo);
        }else{
            log.warn("discard proxy {}",proxyInfo);
        }
    }
}
