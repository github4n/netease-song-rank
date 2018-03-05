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

    @Column(name = "job_id")
    private Integer jobId;

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

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

    @Override
    public String toString() {
        return "UserRefJob{" +
                "id=" + id +
                ", jobAlias='" + jobAlias + '\'' +
                ", jobId=" + jobId +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                ", crtTime=" + crtTime +
                ", updTime=" + updTime +
                '}';
    }
}
