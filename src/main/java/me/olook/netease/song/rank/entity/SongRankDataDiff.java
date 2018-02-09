package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zhaohw
 * @date 2018-02-08 18:15
 */
public class SongRankDataDiff {

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

    @ApiModelProperty(value = "目标用户昵称")
    @Column(name = "target_nickname")
    private String targetNickname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobRecordId() {
        return jobRecordId;
    }

    public void setJobRecordId(String jobRecordId) {
        this.jobRecordId = jobRecordId;
    }

    public Integer getRankChange() {
        return rankChange;
    }

    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getTargetNickname() {
        return targetNickname;
    }

    public void setTargetNickname(String targetNickname) {
        this.targetNickname = targetNickname;
    }

    @Override
    public String toString() {
        return "SongRankDataDiff{" +
                "id=" + id +
                ", jobRecordId='" + jobRecordId + '\'' +
                ", rankChange=" + rankChange +
                ", song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                ", changeTime=" + changeTime +
                ", targetNickname='" + targetNickname + '\'' +
                '}';
    }
}
