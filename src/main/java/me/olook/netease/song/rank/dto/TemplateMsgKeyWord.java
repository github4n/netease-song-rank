package me.olook.netease.song.rank.dto;

import java.io.Serializable;

/**
 * @author zhaohw
 * @date 2018-03-08 14:02
 */
public class TemplateMsgKeyWord implements Serializable{

    public TemplateMsgKeyWord() {
    }

    public TemplateMsgKeyWord(String value1, String value2, String value3) {
        this.keyword1 = new Keyword(value1);
        this.keyword2 = new Keyword(value2);
        this.keyword3 = new Keyword(value3);
    }

    private Keyword keyword1;

    private Keyword keyword2;

    private Keyword keyword3;

    public Keyword getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(Keyword keyword1) {
        this.keyword1 = keyword1;
    }

    public Keyword getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(Keyword keyword2) {
        this.keyword2 = keyword2;
    }

    public Keyword getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(Keyword keyword3) {
        this.keyword3 = keyword3;
    }

}

