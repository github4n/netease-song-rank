package me.olook.netease.song.rank.dto;

import lombok.Data;

/**
 * 模板消息参数
 * @author zhaohw
 * @date 2018-03-08 13:58
 */
@Data
public class TemplateMsgParam {

    private String touser;

    private String template_id;

    private String page;

    private String form_id;

    private TemplateMsgKeyWord data;

    private String emphasis_keyword;

}
