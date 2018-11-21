package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 模板消息
 * @author zhaohw
 * @date 2018-03-08 12:58
 */
@Data
@Entity
@Table(name = "template_msg_record")
public class TemplateMessage {

    public final static int INVALID = 0;

    public final static int VALID = 1;

    public final static int EXPIRED = 2;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @Id
    private Integer id;

    @Column(name = "openid")
    private String openid;

    @Column(name = "target_user_id")
    private String targetUserId;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "page")
    private String page;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "upd_time")
    private Date updTime;

    /**
     * 0:已用 1:可用 2:过期
     */
    @Column(name = "is_valid")
    private Integer isValid;

}
