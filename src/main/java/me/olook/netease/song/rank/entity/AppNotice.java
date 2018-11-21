package me.olook.netease.song.rank.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统公告
 * @author zhaohw
 * @date 2018-07-15 9:38
 */
@Data
@Entity
@Table(name = "app_notice")
public class AppNotice {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    private String content;

    private Date crtTime;

    private Integer delFlag;

    private String type;
}
