package me.olook.netease.song.rank.util.proxy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 代理配置
 * @author zhaohw
 * @date 2018-07-15 13:05
 */
@Data
@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {

    private String orderId;

    private Integer poolSize;
}
