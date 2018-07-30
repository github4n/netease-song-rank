package me.olook.netease.song.rank.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统公告
 * @author zhaohw
 * @date 2018-07-15 9:38
 */
@Data
@Table(name = "app_notice")
public class AppNotice {

    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    private String content;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "del_flag")
    private Integer delFlag;

    private String type;
}
