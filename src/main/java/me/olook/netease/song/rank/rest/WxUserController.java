package me.olook.netease.song.rank.rest;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.biz.WxUserBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-04-02 10:46
 */
@Controller
@RequestMapping("user")
@Api(description = "微信用户模块")
public class WxUserController  extends BaseController<WxUserBiz,WxUser> {

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @Override
    @ApiOperation(value = "新增")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> add(@RequestBody @Valid WxUser entity, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        entity.setCrtTime(new Date());
        int result = baseBiz.insertSelective(entity);
        //唯一键重复
        if(result==-1){
            return ResponseEntity.status(500).body("不能重复添加");
        }
        return ResponseEntity.status(200).body("新增成功");
    }

    @ApiOperation(value = "查询关注的微信用户")
    @RequestMapping(value = "getFollow", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getFollowUser(String targetUserId,Integer delFlag) {
        Example example = new Example(UserRefJob.class);
        if(delFlag!=null){
            example.createCriteria().andEqualTo("targetUserId",targetUserId).andEqualTo("delFlag",delFlag);
        }else{
            example.createCriteria().andEqualTo("targetUserId",targetUserId);
        }
        List<UserRefJob> jobList = userRefJobBiz.selectByExample(example);
        List<WxUser> users = new ArrayList<>();
        for(UserRefJob job : jobList){
            Example userExample = new Example(WxUser.class);
            userExample.createCriteria().andEqualTo("openId",job.getOpenId());
            List<WxUser> user = baseBiz.selectByExample(userExample);
            if(user.size()>0){
                users.add(user.get(0));
            }
        }
        String json = JSONObject.toJSONString(users);
        return ResponseEntity.status(200).body(json);
    }
}
