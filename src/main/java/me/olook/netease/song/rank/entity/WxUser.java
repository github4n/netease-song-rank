package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
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

    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    /**
     * 推送时间间隔（分钟）
     * 最大间隔为1天
     */
    @Max(value = 60*24)
    @Column(name = "push_interval")
    private Integer pushInterval;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_time")
    private Date crtTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_time")
    private Date updTime;

}
