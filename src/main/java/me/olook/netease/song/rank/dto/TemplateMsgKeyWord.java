package me.olook.netease.song.rank.dto;

/**
 * @author zhaohw
 * @date 2018-03-08 14:02
 */
public class TemplateMsgKeyWord {

    public TemplateMsgKeyWord() {
    }

    public TemplateMsgKeyWord(String value1, String value2, String value3) {
        this.keyword1 = new KeyWord(value1);
        this.keyword2 = new KeyWord(value2);
        this.keyword3 = new KeyWord(value3);
    }

    private KeyWord keyword1;

    private KeyWord keyword2;

    private KeyWord keyword3;

    public KeyWord getKeyWord1() {
        return keyword1;
    }

    public void setKeyWord1(KeyWord keyword1) {
        this.keyword1 = keyword1;
    }

    public KeyWord getKeyWord2() {
        return keyword2;
    }

    public void setKeyWord2(KeyWord keyword2) {
        this.keyword2 = keyword2;
    }

    public KeyWord getKeyWord3() {
        return keyword3;
    }

    public void setKeyWord3(KeyWord keyword3) {
        this.keyword3 = keyword3;
    }
}

