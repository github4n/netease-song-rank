package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 定时任务参数
 *
 * @author zhaohw
 * @date 2018-01-10 17:21
 */

@Table(name = "song_rank_job_record")
public class TimerJobRecord {

    @ApiModelProperty(hidden = true)
    @Id
    private String id;

    @ApiModelProperty(value = "任务id")
    @Column(name = "job_id")
    private Integer jobId;

    @ApiModelProperty(value = "开始时间")
    @Column(name = "start_time")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @Column(name = "end_time")
    private Date endTime;

    @ApiModelProperty(value = "数据是否更新")
    @Column(name = "new_data")
    private Integer newData;

    @ApiModelProperty(value = "数据快照")
    @Column(name = "snapshot")
    private String snapshot;

    @ApiModelProperty(value = "播放计数")
    @Column(name = "count")
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getNewData() {
        return newData;
    }

    public void setNewData(Integer newData) {
        this.newData = newData;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TimerJobRecord{" +
                "id='" + id + '\'' +
                ", jobId=" + jobId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", newData=" + newData +
                ", snapshot='" + snapshot + '\'' +
                ", count=" + count +
                '}';
    }
}
