package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.olook.netease.song.rank.base.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 爬虫定时任务
 * @author zhaohw
 * @date 2018-01-10 17:16
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "song_rank_job")
public class TimerJob extends BaseCreateAndUpdateEntity {

    public final static int STATUS_EXPIRED = 2;

    public final static int STATUS_RUN = 1;

    public final static int STATUS_STOP = 0;

    @ApiModelProperty(value = "任务状态：0暂停 1运行")
    private Integer status;

    @ApiModelProperty(value = "任务名")
    private String jobName;

    @ApiModelProperty(value = "任务组")
    private String jobGroup;

    @ApiModelProperty(value = "任务类型")
    private String jobType;

    @ApiModelProperty(value = "表达式")
    private String cronExpression;

    @ApiModelProperty(value = "目标用户id")
    private String targetUserId;

    @ApiModelProperty(value = "目标用户昵称")
    private String targetNickname;

    @ApiModelProperty(value = "备注")
    private String remark;

}
