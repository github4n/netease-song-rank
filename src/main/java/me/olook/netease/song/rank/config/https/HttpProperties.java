package me.olook.netease.song.rank.config.https;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhaohw
 * @date 2018-03-05 0:07
 */
@Data
@ConfigurationProperties(prefix = "http")
public class HttpProperties {

    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}