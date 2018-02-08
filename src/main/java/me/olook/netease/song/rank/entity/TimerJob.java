package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import me.olook.netease.song.rank.base.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 定时任务
 *
 * @author zhaohw
 * @date 2018-01-10 17:16
 */

@Table(name = "song_rank_job")
public class TimerJob extends BaseCreateAndUpdateEntity {


    @ApiModelProperty(value = "任务状态：0暂停 1运行")
    private Integer status;

    @ApiModelProperty(value = "任务名")
    @Column(name = "job_name")
    private String jobName;

    @ApiModelProperty(value = "任务组")
    @Column(name = "job_group")
    private String jobGroup;

    @ApiModelProperty(value = "任务类型")
    @Column(name = "job_type")
    private String jobType;

    @ApiModelProperty(value = "表达式")
    @Column(name = "cron_expression")
    private String cronExpression;

    @ApiModelProperty(value = "目标用户id")
    @Column(name = "target_userid")
    private String targetUserid;

    @ApiModelProperty(value = "目标用户昵称")
    @Column(name = "target_nickname")
    private String targetNickname;

    @ApiModelProperty(value = "备注")
    private String remark;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getTargetUserid() {
        return targetUserid;
    }

    public void setTargetUserid(String targetUserid) {
        this.targetUserid = targetUserid;
    }

    public String getTargetNickname() {
        return targetNickname;
    }

    public void setTargetNickname(String targetNickname) {
        this.targetNickname = targetNickname;
    }
}
