package me.olook.netease.song.rank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhaohw
 * @date 2018-03-05 14:11
 */
@Data
@ConfigurationProperties(prefix = "wxapp")
public class WxAppProperties {

    private String appId;

    private String appSecret;

    private String grantType ;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
