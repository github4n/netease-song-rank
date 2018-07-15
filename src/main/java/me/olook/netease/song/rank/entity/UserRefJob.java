package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户任务关联
 *
 * @author zhaohw
 * @date 2018-03-05 20:39
 */
@Data
public class UserRefJob {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @Column(name = "job_alias")
    private String jobAlias;

    @Column(name = "target_userid")
    private String targetUserId;

    @Column(name = "target_nickname")
    private String targetNickname;

    @Column(name = "target_avatar")
    private String targetAvatar;

    @Column(name = "openid")
    private String openId;

    @Column(name = "unionid")
    private String unionId;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_time")
    private Date crtTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_time")
    private Date updTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "del_flag")
    private Integer delFlag;

}
