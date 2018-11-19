package me.olook.netease.song.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaohw
 * @date 2018-07-15 11:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongRankDataDTO {

    private String songName;

    private String singerName;

    private Integer rank;

    private String ratio;

    /**
     * 重写ToString
     * 计算快照时使用
     */
    @Override
    public String toString(){
        return songName + singerName + rank + ratio;
    }
}
