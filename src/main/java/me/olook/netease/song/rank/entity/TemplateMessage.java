package me.olook.netease.song.rank.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 模板消息
 *
 * @author zhaohw
 * @date 2018-03-08 12:58
 */
@Table(name = "template_msg_record")
public class TemplateMessage {

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

    @Column(name = "is_valid")
    private Integer isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
