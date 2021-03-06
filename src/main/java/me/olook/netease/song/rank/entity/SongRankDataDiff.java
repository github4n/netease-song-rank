package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 排行榜变化数据
 * @author zhaohw
 * @date 2018-02-08 18:15
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "song_rank_data_diff")
public class SongRankDataDiff {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @ApiModelProperty(value = "定时任务记录id")
    @Column(name = "job_record_id")
    private String jobRecordId;

    @ApiModelProperty(value = "排序变化")
    @Column(name = "rank_change")
    private Integer rankChange;

    @ApiModelProperty(value = "歌名")
    @Column(name = "song")
    private String song;

    @ApiModelProperty(value = "歌手")
    @Column(name = "singer")
    private String singer;

    @ApiModelProperty(value = "变化时间")
    @Column(name = "change_time")
    private Date changeTime;

    @ApiModelProperty(value = "目标用户id")
    @Column(name = "target_userid")
    private String targetUserId;

    @ApiModelProperty(value = "封面")
    @Column(name = "pic_url")
    private String picUrl;

    @ApiModelProperty(value = "歌曲id")
    @Column(name = "song_id")
    private Integer songId;

    @Transient
    private Integer count;

    @Transient
    private String targetUserName;

    @ApiModelProperty(value = "是否系统批量更新")
    @Column(name = "is_batch_update")
    private Integer isBatchUpdate;

}
