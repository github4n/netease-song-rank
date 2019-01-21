package me.olook.netease.song.rank.exception;

/**
 * @author zhaohw
 * @date 2019-01-21 15:52
 */
public class DataResolveException extends Exception{

    private Integer code;

    public DataResolveException() {
    }

    public DataResolveException(String message) {
        super(message);
    }

    public DataResolveException(Integer code) {
        this.code = code;
    }

    public DataResolveException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
