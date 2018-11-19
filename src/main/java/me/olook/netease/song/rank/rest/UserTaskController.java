package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.UserTaskBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.exception.UserTaskException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户任务
 * @author zhaohw
 * @date 2018-11-16 14:24
 */
@RestController
@RequestMapping("task")
@Api(description = "用户任务模块")
public class UserTaskController {

    private final UserTaskBiz userTaskBiz;

    @Autowired
    public UserTaskController(UserTaskBiz userTaskBiz) {
        this.userTaskBiz = userTaskBiz;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "")
    public ResponseEntity add(@RequestBody @Valid UserRefJob userRefJob, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        try {
             int index = userTaskBiz.addTask(userRefJob);
             return ResponseEntity.status(200).body("新增成功，您是第"+index+"位订阅Ta的好友!");
        } catch (UserTaskException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @ApiOperation(value = "取消关注用户")
    @DeleteMapping(value = "{id}")
    public ResponseEntity remove(@PathVariable Integer id){
        boolean result = userTaskBiz.removeTask(id);
        if(result){
            return ResponseEntity.status(200).body("删除成功");
        }else{
            return ResponseEntity.status(500).body("删除失败");
        }
    }
}