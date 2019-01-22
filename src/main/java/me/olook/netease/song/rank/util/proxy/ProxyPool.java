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

    public static ThreadPoolExecutor executors = new ThreadPoolExecutor(5, 5,0L,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(90),new ThreadPoolExecutor.DiscardOldestPolicy());



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

    public static int activeSize(){
        return proxyQueue.size();
    }
}
