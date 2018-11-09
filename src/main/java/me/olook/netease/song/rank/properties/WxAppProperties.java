package me.olook.netease.song.rank.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author zhaohw
 * @date 2018-11-09 14:50
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "wxapp")
public class WxAppProperties {

    private String appId;

    private String appSecret;

    private String grantType;
}
