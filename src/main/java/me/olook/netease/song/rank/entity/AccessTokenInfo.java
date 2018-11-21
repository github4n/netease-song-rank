package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信API Access Token
 * @author zhaohw
 * @date 2018-03-08 13:34
 */
@Data
@Entity
@Table(name = "access_token_info")
public class AccessTokenInfo {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    private String accessToken;

    private Date crtTime;

    private Date invalidTime;

    private Integer isValid;

    public AccessTokenInfo() {
    }

    public AccessTokenInfo(String accessToken) {
        this.accessToken = accessToken;
        this.isValid = 1;
        this.crtTime = new Date();
        this.invalidTime = new Date(System.currentTimeMillis()+2*60*60*1000-60);
    }

}
