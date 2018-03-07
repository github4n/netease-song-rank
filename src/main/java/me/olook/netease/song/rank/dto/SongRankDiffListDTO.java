package me.olook.netease.song.rank.dto;

import me.olook.netease.song.rank.entity.SongRankDataDiff;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-07 9:36
 */
public class SongRankDiffListDTO {

    private Integer isBatchUpdate;

    private List<SongRankDataDiff> list;

    public Integer getIsBatchUpdate() {
        return isBatchUpdate;
    }

    public void setIsBatchUpdate(Integer isBatchUpdate) {
        this.isBatchUpdate = isBatchUpdate;
    }

    public List<SongRankDataDiff> getList() {
        return list;
    }

    public void setList(List<SongRankDataDiff> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SongRankDiffDTO{" +
                "isBatchUpdate=" + isBatchUpdate +
                ", list=" + list +
                '}';
    }
}
