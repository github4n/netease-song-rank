package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.olook.netease.song.rank.base.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 定时任务
 *
 * @author zhaohw
 * @date 2018-01-10 17:16
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "song_rank_job")
public class TimerJob extends BaseCreateAndUpdateEntity {

    public final static Integer STATUS_EXPIRED = 2;

    public final static Integer STATUS_RUN = 1;

    public final static Integer STATUS_STOP = 0;

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

}
