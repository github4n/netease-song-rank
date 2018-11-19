package me.olook.netease.song.rank.dto;

import lombok.Data;
import me.olook.netease.song.rank.entity.SongRankDataDiff;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-07 9:36
 */
@Data
public class SongRankDiffListDTO {

    private Integer isBatchUpdate;

    private List<SongRankDataDiff> list;

}
