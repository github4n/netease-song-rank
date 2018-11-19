package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 排行榜数据
 * @author zhaohw
 * @date 2018-02-08 15:01
 */
@Data
@Entity
@NoArgsConstructor
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

    @ApiModelProperty(value = "类型")
    @Column(name = "genre")
    private String genre;

    @ApiModelProperty(value = "封面")
    @Column(name = "pic_url")
    private String picUrl;

    public SongRankData(Integer rank, String song, String singer, String ratio, String genre, String picUrl) {
        this.rank = rank;
        this.song = song;
        this.singer = singer;
        this.ratio = ratio;
        this.genre = genre;
        this.picUrl = picUrl;
    }

    public SongRankData(String jobRecordId, Integer rank, String song, String singer, String ratio, String genre, String picUrl) {
        this.jobRecordId = jobRecordId;
        this.rank = rank;
        this.song = song;
        this.singer = singer;
        this.ratio = ratio;
        this.genre = genre;
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "SongRankData{" +
                "rank=" + rank +
                ", song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                ", ratio='" + ratio + '\'' +
                '}';
    }
}
