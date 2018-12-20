package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.TemplateMessageBiz;
import me.olook.netease.song.rank.entity.TemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaohw
 * @date 2018-12-20 16:49
 */
@RestController
@RequestMapping("msg")
@Api(description = "模板消息模块")
public class TemplateMessageController {

    private final TemplateMessageBiz templateMessageBiz;

    @Autowired
    public TemplateMessageController(TemplateMessageBiz templateMessageBiz) {
        this.templateMessageBiz = templateMessageBiz;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public ResponseEntity<String> add(@RequestBody TemplateMessage entity){
        // todo  zhaohw 订阅/关注订阅列表 2018-12-20 17:14
        return null;
    }
}
