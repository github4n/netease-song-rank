package me.olook.netease.song.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送模板参数
 * @author zhaohw
 * @date 2018-03-08 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class Keyword {

    private String value;

    private String color;

    Keyword(String value) {
        this.value = value;
    }
}
