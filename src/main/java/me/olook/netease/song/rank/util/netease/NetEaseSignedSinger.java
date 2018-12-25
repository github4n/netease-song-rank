package me.olook.netease.song.rank.util.netease;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

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

    public String toSql(){
        return "INSERT INTO `song_rank_job` VALUES (null, '1', '"+id+"', 'group2', '听歌排行爬取任务', '"+new Random().nextInt(60)+" */1 * * * ?', '"+id+"', '"+name+"', null, '2018-12-25 14:07:38', 'system', null, '2018-12-25 14:07:38', null, 'new job');";
    }

}
