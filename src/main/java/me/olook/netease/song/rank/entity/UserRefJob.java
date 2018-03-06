package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户任务关联
 *
 * @author zhaohw
 * @date 2018-03-05 20:39
 */
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobAlias() {
        return jobAlias;
    }

    public void setJobAlias(String jobAlias) {
        this.jobAlias = jobAlias;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetNickname() {
        return targetNickname;
    }

    public void setTargetNickname(String targetNickname) {
        this.targetNickname = targetNickname;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }
}
