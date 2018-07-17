package me.olook.netease.song.rank.exception;

/**
 * 代理失效异常
 * @author zhaohw
 * @date 2018-07-17 16:16
 */
public class ProxyInvalidException extends RuntimeException{

    public ProxyInvalidException(String message) {
        super(message);
    }
}
