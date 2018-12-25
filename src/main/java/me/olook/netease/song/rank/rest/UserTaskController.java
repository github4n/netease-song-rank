package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.biz.UserTaskBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.exception.UserTaskException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户任务
 * @author zhaohw
 * @date 2018-11-16 14:24
 */
@RestController
@RequestMapping("task")
@Api(description = "用户任务模块")
public class UserTaskController {

    private final UserRefJobBiz userRefJobBiz;

    private final UserTaskBiz userTaskBiz;

    private final TimerJobBiz timerJobBiz;

    @Autowired
    public UserTaskController(UserTaskBiz userTaskBiz, UserRefJobBiz userRefJobBiz, TimerJobBiz timerJobBiz) {
        this.userTaskBiz = userTaskBiz;
        this.userRefJobBiz = userRefJobBiz;
        this.timerJobBiz = timerJobBiz;
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

    @ApiOperation(value = "取消关注")
    @DeleteMapping(value = "{id}")
    public ResponseEntity remove(@PathVariable Integer id){
        boolean result = userTaskBiz.removeTask(id);
        if(result){
            return ResponseEntity.status(200).body("删除成功");
        }else{
            return ResponseEntity.status(500).body("删除失败");
        }
    }

    @ApiOperation(value = "获取任务")
    @GetMapping(value = "")
    public ResponseEntity getByOpenId(String openid) {
        List<UserRefJob> jobList = userRefJobBiz.findByOpenIdAndDelFlag(openid,0);
        return ResponseEntity.status(200).body(jobList);
    }

    @ApiOperation(value = "获取分组任务")
    @GetMapping(value = "group/{jobGroup}")
    public ResponseEntity getByGroup(@PathVariable String jobGroup,@RequestParam(defaultValue = "10") Integer limit) {
        List<TimerJob> jobList = timerJobBiz.findByJobGroupAndStatus(jobGroup,TimerJob.STATUS_RUN);
        List<TimerJob> reduceList = jobList.stream().limit(limit).collect(Collectors.toList());
        return ResponseEntity.status(200).body(reduceList);
    }
}
