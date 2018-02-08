package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhaohw
 * @date 2018-02-08 15:01
 */
@Table(name = "song_rank_data")
public class SongRankData {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @ApiModelProperty(value = "定时任务记录id")
    @Column(name = "job_record_id")
    private String jobRecordId;

    @ApiModelProperty(value = "排序")
    @Column(name = "rank")
    private Integer rank;

    @ApiModelProperty(value = "歌名")
    @Column(name = "song")
    private String song;

    @ApiModelProperty(value = "歌手")
    @Column(name = "singer")
    private String singer;

    @ApiModelProperty(value = "占比")
    @Column(name = "ratio")
    private String ratio;

    public SongRankData() {
    }

    public SongRankData(String jobRecordId, Integer rank, String song, String singer, String ratio) {
        this.jobRecordId = jobRecordId;
        this.rank = rank;
        this.song = song;
        this.singer = singer;
        this.ratio = ratio;
    }

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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "SongRankData{" +
                "id=" + id +
                ", jobRecordId=" + jobRecordId +
                ", rank=" + rank +
                ", song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
