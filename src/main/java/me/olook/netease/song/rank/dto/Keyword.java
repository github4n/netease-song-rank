package me.olook.netease.song.rank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送模板参数
 * @author zhaohw
 * @date 2018-03-08 15:32
 */
@Data
@NoArgsConstructor
public class Keyword {

    private String value;

    private String color;

    public Keyword(String value) {
        this.value = value;
    }
}
