package me.olook.netease.song.rank.util.proxy;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhaohw
 * @date 2018-11-19 14:48
 */
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
    static ConcurrentLinkedQueue<ProxyInfo> failQueue = new ConcurrentLinkedQueue<ProxyInfo>();

}
