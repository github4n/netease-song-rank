package me.olook.netease.song.rank.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author zhaohw
 * @date 2018-11-16 14:14
 */
@Data
public class TemplateMsgParam {

    @JSONField(name = "touser")
    private String toUser;

    @JSONField(name = "template_id")
    private String templateId;

    private String page;

    @JSONField(name = "form_id")
    private String formId;

    private TemplateMsgKeyWord data;

    @JSONField(name = "emphasis_keyword")
    private String emphasisKeyword;

}
