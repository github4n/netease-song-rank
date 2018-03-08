package me.olook.netease.song.rank.dto;

/**
 * @author zhaohw
 * @date 2018-03-08 13:58
 */
public class TemplateMsgParam {

    private String touser;

    private String template_id;

    private String page;

    private String form_id;

    private TemplateMsgKeyWord data;

    private String emphasis_keyword;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public TemplateMsgKeyWord getData() {
        return data;
    }

    public void setData(TemplateMsgKeyWord data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }
}
