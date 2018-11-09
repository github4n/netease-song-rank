package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 微信用户
 * @author zhaohw
 * @date 2018-04-02 10:37
 */
@Data
@Entity
@Table(name = "wx_user")
public class WxUser {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @Column(name = "nickName")
    private String nickName;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "avatarUrl")
    private String avatarUrl;

    @Column(name = "country")
    private String country;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "language")
    private String language;

    @Column(name = "openid")
    private String openId;

    @Column(name = "job_limit")
    private Integer jobLimit;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_time")
    private Date crtTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_time")
    private Date updTime;

}
