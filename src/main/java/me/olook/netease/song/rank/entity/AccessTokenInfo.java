package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhaohw
 * @date 2018-03-08 13:34
 */
@Data
@Table(name = "access_token_info")
public class AccessTokenInfo {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "invalid_time")
    private Date invalidTime;

    @Column(name = "is_valid")
    private Integer isValid;

    public AccessTokenInfo() {
    }

    public AccessTokenInfo(String accessToken) {
        this.accessToken = accessToken;
        this.isValid = 1;
        this.crtTime = new Date();
        //提前1分钟重新获取
        this.invalidTime = new Date(System.currentTimeMillis()+2*60*60*1000-60);
    }

}
