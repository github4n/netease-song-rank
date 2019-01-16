package me.olook.netease.song.rank.util.proxy;

import java.util.List;
import java.util.concurrent.*;

/**
* 单队列代理池
* @author red
*/
public class ProxyPool {

    /**
     * 代理队列
     */
    private static ConcurrentLinkedQueue<ProxyInfo> proxyQueue = new ConcurrentLinkedQueue<ProxyInfo>();

    public static ExecutorService executors = new ThreadPoolExecutor(5, 5,0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * 从队列中获取一个代理
     * @return 代理
     */
    public static ProxyInfo poll(){
        return proxyQueue.poll();
    }

    /**
     * 添加一个代理至队列
     * @param proxy 代理信息
     */
    public static void offer(ProxyInfo proxy){
        if(proxy != null){
            proxyQueue.offer(proxy);
        }
    }

    public static void offer(List<ProxyInfo> proxyInfoList){
        proxyInfoList.forEach(p->proxyQueue.offer(p));
    }
}
