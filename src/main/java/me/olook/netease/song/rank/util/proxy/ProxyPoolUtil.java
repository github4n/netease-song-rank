package me.olook.netease.song.rank.util.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhaohw
 * @date 2018-11-19 14:48
 */
@Slf4j
public class ProxyPoolUtil {

    /**
     * 工作队列 存储代理信息
     */
    static ConcurrentLinkedQueue<ProxyInfo> workQueue = new ConcurrentLinkedQueue<ProxyInfo>();

    /**
     * 待删除队列
     *  请求失败时查询代理是否处于此队列
     *  不存在则添加
     *  存在则删除代理信息
     */
    private static ConcurrentLinkedQueue<ProxyInfo> failQueue = new ConcurrentLinkedQueue<ProxyInfo>();


    public static ProxyInfo get(){
        return workQueue.poll();
    }

    /**
     * 请求成功是调用
     *      归还代理至工作队列，若失败队列中存在，将它从失败队列删除
     */
    public static void restore(ProxyInfo proxy){
        if(proxy != null){
            workQueue.offer(proxy);
            failQueue.remove(proxy);
        }

    }

    /**
     * 请求失败时调用
     *      失败队列中已存在，从失败队列中删除，直接舍弃
     *      失败队列中不存在，添加到失败队列和工作队列，重新提供一次机会
     */
    public static void fail(ProxyInfo proxy){
        if (failQueue.contains(proxy)){
            failQueue.remove(proxy);
        }else {
            workQueue.offer(proxy);
            failQueue.offer(proxy);
        }
    }

    public static void debugInfo(){
        log.debug("----------Proxy Pool Info------------");
        log.debug("work queue size : {}",workQueue.size());
        log.debug(workQueue.toString());
        log.debug("fail queue size : {}",failQueue.size());
        log.debug(failQueue.toString());
        log.debug("-------------------------------------");
    }

    public static int activeSize(){
        return workQueue.size()-failQueue.size();
    }

    public static ConcurrentLinkedQueue<ProxyInfo> getWorkQueue() {
        return workQueue;
    }

    public static ConcurrentLinkedQueue<ProxyInfo> getFailQueue() {
        return failQueue;
    }
}
