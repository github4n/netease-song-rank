package me.olook.netease.song.rank.base;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-08 16:58
 */
public class BaseController<T> {

    @Resource
    protected T biz;
}
