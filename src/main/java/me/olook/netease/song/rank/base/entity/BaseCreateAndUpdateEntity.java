package me.olook.netease.song.rank.base.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.util.Date;

/**
 * 创建时间 创建用户 创建用户名
 * 更新时间 更新用户 更新用户名
 * @author zhaohw
 * @date 2017-11-10 11:47
 */
public class BaseCreateAndUpdateEntity extends BaseCreateEntity{

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_time")
    private Date updTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_user")
    private String updUser;

    @ApiModelProperty(hidden = true)
    @Column(name = "upd_name")
    private String updName;

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public String getUpdName() {
        return updName;
    }

    public void setUpdName(String updName) {
        this.updName = updName;
    }
}
