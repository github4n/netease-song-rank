package me.olook.netease.song.rank.base.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 创建时间 创建用户 创建用户名 创建ip
 * 更新时间 更新用户 更新用户名 更新ip
 * @author zhaohw
 * @date 2017-11-10 11:52
 */
public class BaseCreateAndUpdateHostEntity extends BaseCreateAndUpdateEntity{

    @ApiModelProperty(hidden = true)
    private String crtHost;

    @ApiModelProperty(hidden = true)
    private String updHost;

    public String getCrtHost() {
        return crtHost;
    }

    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    public String getUpdHost() {
        return updHost;
    }

    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }
}
