package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 爬虫定时任务记录
 * @author zhaohw
 * @date 2018-01-10 17:21
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
