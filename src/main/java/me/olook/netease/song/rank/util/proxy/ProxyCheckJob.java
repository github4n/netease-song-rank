package me.olook.netease.song.rank.util.proxy;

import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;

/**
 * @author zhaohw
 */
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
        }
    }
}
