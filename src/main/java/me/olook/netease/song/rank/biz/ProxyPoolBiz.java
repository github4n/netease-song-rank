package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.util.proxy.ProxyHttpClient;
import me.olook.netease.song.rank.util.proxy.ProxyProvider;
import me.olook.netease.song.rank.util.proxy.gather.GatherProxyProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-19 15:06
 */
@Service
public class ProxyPoolBiz {

    @Resource
    private ProxyHttpClient proxyHttpClient;

    public void fixProxyPool(){
        ProxyProvider proxyProvider = new GatherProxyProvider();
        proxyProvider.fixProxyPool();
    }
}
