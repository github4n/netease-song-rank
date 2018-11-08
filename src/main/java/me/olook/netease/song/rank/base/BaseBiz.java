package me.olook.netease.song.rank.base;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-08 16:28
 */
public class BaseBiz<R extends JpaRepository>{

    @Resource
    protected R repository;
}
