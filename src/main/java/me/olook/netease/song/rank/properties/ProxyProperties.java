package me.olook.netease.song.rank.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhaohw
 * @date 2018-11-19 14:26
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {

    private String orderId;

    private String params;

    /**
     * 代理池活跃数量大小
     * 为 0 则不使用代理
     */
    private Integer poolSize;
}
