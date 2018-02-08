package me.olook.netease.song.rank.base.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 创建时间 创建用户 创建用户名
 * @author zhaohw
 * @date 2017-11-10 11:47
 */
public class BaseCreateEntity {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_time")
    private Date crtTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_user")
    private String crtUser;

    @ApiModelProperty(hidden = true)
    @Column(name = "crt_name")
    private String crtName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }
}
