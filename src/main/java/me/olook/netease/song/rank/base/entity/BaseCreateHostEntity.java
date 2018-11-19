package me.olook.netease.song.rank.base.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.MappedSuperclass;

/**
 * 创建时间 创建用户 创建用户名 创建ip
 * @author zhaohw
 * @date 2017-11-10 11:51
 */
@MappedSuperclass
public class BaseCreateHostEntity extends BaseCreateEntity{

    @ApiModelProperty(hidden = true)
    private String crtHost;

    public String getCrtHost() {
        return crtHost;
    }

    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }
}
