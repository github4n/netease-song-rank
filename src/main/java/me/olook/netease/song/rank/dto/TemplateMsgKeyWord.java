package me.olook.netease.song.rank.dto;

/**
 * @author zhaohw
 * @date 2018-03-08 14:02
 */
public class TemplateMsgKeyWord {

    public TemplateMsgKeyWord(String value1, String value2, String value3) {
        this.keyWord1.value = value1;
        this.keyWord2.value = value2;
        this.keyWord3.value = value3;
    }

    private KeyWord keyWord1;

    private KeyWord keyWord2;

    private KeyWord keyWord3;

    public KeyWord getKeyWord1() {
        return keyWord1;
    }

    public void setKeyWord1(KeyWord keyWord1) {
        this.keyWord1 = keyWord1;
    }

    public KeyWord getKeyWord2() {
        return keyWord2;
    }

    public void setKeyWord2(KeyWord keyWord2) {
        this.keyWord2 = keyWord2;
    }

    public KeyWord getKeyWord3() {
        return keyWord3;
    }

    public void setKeyWord3(KeyWord keyWord3) {
        this.keyWord3 = keyWord3;
    }
}

class KeyWord{
     String value;
     String color;

}
