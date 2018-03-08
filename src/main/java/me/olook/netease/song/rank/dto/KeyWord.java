package me.olook.netease.song.rank.dto;

/**
 * @author zhaohw
 * @date 2018-03-08 15:32
 */
public class KeyWord {
     private String value;
     private String color;

    public KeyWord() {

    }

    public KeyWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
