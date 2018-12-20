package me.olook.netease.song.rank.util.netease;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhaohw
 * @date 2018-12-17 16:26
 */
@Setter
@Getter
@AllArgsConstructor
public class NetEaseSignedSinger {

    private String id;

    private String name;

    private Integer rankNum;

    @Override
    public String toString() {
        return id+" : "+name+" : "+ rankNum;
    }
}
