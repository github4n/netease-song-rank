package me.olook.netease.song.rank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhaohw
 * @date 2018-03-08 14:02
 */
@NoArgsConstructor
@Data
public class TemplateMsgKeyWord implements Serializable{

    private Keyword keyword1;

    private Keyword keyword2;

    private Keyword keyword3;

    public TemplateMsgKeyWord(String value1, String value2, String value3) {
        this.keyword1 = new Keyword(value1);
        this.keyword2 = new Keyword(value2);
        this.keyword3 = new Keyword(value3);
    }

}

