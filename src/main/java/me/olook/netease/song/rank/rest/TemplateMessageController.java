package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.TemplateMessageBiz;
import me.olook.netease.song.rank.entity.TemplateMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-08 13:08
 */
@Controller
@RequestMapping("msg")
@Api(description = "模板消息模块")
public class TemplateMessageController
        extends BaseController<TemplateMessageBiz,TemplateMessage>{
    @Override
    public ResponseEntity<String> add(@RequestBody @Valid TemplateMessage templateMessage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        templateMessage.setCrtTime(new Date());
        templateMessage.setPage("record?userId="+templateMessage.getTargetUserId());
        templateMessage.setTemplateId("AIL1AXTIKfmmifc4uPpCthIiNi-AMgMSxXBvXihnPOg");
        templateMessage.setIsValid(1);
        int result = baseBiz.insertSelective(templateMessage);
        //唯一键重复
        if(result==-1){
            return ResponseEntity.status(500).body("不能重复添加");
        }
        return ResponseEntity.status(200).body("订阅成功!Ta在本周内首次听歌会通知您~");
    }
}
