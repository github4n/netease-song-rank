package me.olook.netease.song.rank.exception;

/**
 * 用户关闭排行权限异常
 * @author zhaohw
 * @date 2018-07-15 14:15
 */
public class PermissionDeniedException extends RuntimeException{

    public PermissionDeniedException(String message) {
        super(message);
    }
}
