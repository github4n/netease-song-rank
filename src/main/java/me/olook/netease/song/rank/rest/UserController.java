package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.biz.WxUserBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-04-02 10:46
 */
@RestController
@RequestMapping("user")
@Api(description = "微信用户模块")
public class UserController{

    private final WxUserBiz wxUserBiz;

    private final UserRefJobBiz userRefJobBiz;

    @Autowired
    public UserController(WxUserBiz wxUserBiz, UserRefJobBiz userRefJobBiz) {
        this.wxUserBiz = wxUserBiz;
        this.userRefJobBiz = userRefJobBiz;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public ResponseEntity<String> add(@RequestBody @Valid WxUser user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        user.setCrtTime(new Date());
        WxUser result = wxUserBiz.save(user);
        return ResponseEntity.status(200).body("新增成功");
    }

    @ApiOperation(value = "查询关注的微信用户")
    @GetMapping(value = "follow")
    public ResponseEntity getFollowUser(String targetUserId,@RequestParam(defaultValue = "0") Integer delFlag) {
        List<UserRefJob> jobList = userRefJobBiz.findByTargetUserIdAndDelFlag(targetUserId,delFlag);
        List<WxUser> users = new ArrayList<>();
        for(UserRefJob job : jobList){
            users.add(wxUserBiz.findByOpenId(job.getOpenId()));
        }
        return ResponseEntity.status(200).body(users);
    }
}
