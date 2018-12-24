package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.TemplateMessageBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.dto.SubscribeNetEaseUserDTO;
import me.olook.netease.song.rank.entity.TemplateMessage;
import me.olook.netease.song.rank.entity.UserRefJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaohw
 * @date 2018-12-20 16:49
 */
@RestController
@RequestMapping("msg")
@Api(description = "模板消息模块")
public class TemplateMessageController {

    private final UserRefJobBiz userRefJobBiz;

    private final TemplateMessageBiz templateMessageBiz;

    @Autowired
    public TemplateMessageController(TemplateMessageBiz templateMessageBiz, UserRefJobBiz userRefJobBiz) {
        this.templateMessageBiz = templateMessageBiz;
        this.userRefJobBiz = userRefJobBiz;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public ResponseEntity add(@RequestBody TemplateMessage templateMessage){
        TemplateMessage res = templateMessageBiz.addTemplates(templateMessage);
        if(res == null){
            return ResponseEntity.status(500).body("订阅次数不能再多了");
        }
        return ResponseEntity.ok().body("订阅成功");
    }

    @ApiOperation(value = "订阅列表")
    @GetMapping(value = "")
    public ResponseEntity list(String openid){
        List<UserRefJob> jobList = userRefJobBiz.findByOpenIdAndDelFlag(openid,0);
        List<TemplateMessage> templatesList = templateMessageBiz.findValidTemplatesByOpenid(openid);
        //查询每个用户的可用模板记录
        List<SubscribeNetEaseUserDTO> collect = jobList.stream().map(job -> {
            long count = templatesList.stream()
                    .filter(templateMessage -> templateMessage.getTargetUserId().equals(job.getTargetUserId()))
                    .count();
            return new SubscribeNetEaseUserDTO(job.getTargetUserId(), job.getTargetAvatar(), job.getTargetNickname(), count);
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @ApiOperation(value = "取消订阅")
    @DeleteMapping(value = "")
    public ResponseEntity cancel(String openid,String targetUserId){
        List<TemplateMessage> templates = templateMessageBiz.findValidTemplatesByOpenid(openid);
        templates.stream().filter(t->t.getTargetUserId().equals(targetUserId))
                .forEach(templateMessage -> {
                templateMessage.setIsValid(TemplateMessage.DELETED);
                templateMessage.setUpdTime(new Date());
                templateMessageBiz.save(templateMessage);
        });
        return ResponseEntity.ok().body("取消订阅成功");
    }
}
