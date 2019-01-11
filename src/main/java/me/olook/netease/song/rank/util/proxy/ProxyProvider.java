package me.olook.netease.song.rank.util.proxy;

import java.util.List;

/**
 * 代理提供者
 * @author zhaohw
 * @date 2019-01-11 16:49
 */
public interface ProxyProvider {

    /**
     * 请求代理数据
     * @return HTML/JSON/TXT
     */
    String requestForPayload();

    /**
     * 从载荷中解析代理数据
     * @param payload 载荷
     * @return 代理信息
     */
    List<ProxyInfo> resolveProxy(String payload);
}
