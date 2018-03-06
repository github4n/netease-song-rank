package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-05 20:47
 */
@Controller
@RequestMapping("userjob")
@Api(description = "用户任务模块")
public class UserRefJobController extends BaseController<UserRefJobBiz,UserRefJob> {

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseEntity<String> add(@RequestBody @Valid UserRefJob userRefJob, BindingResult bindingResult) {
        Example example = new Example(TimerJob.class);
        example.createCriteria().andEqualTo("targetUserid",userRefJob.getTargetUserId());
        List<TimerJob> timerJobs = timerJobBiz.selectByExample(example);
        //未创建该目标对象的任务
        if(timerJobs.size()==0){
            TimerJob newJob = new TimerJob();
            newJob.setCronExpression("0 */1 * * * ?");
            newJob.setJobName(userRefJob.getTargetUserId());
            newJob.setJobGroup("group1");
            newJob.setStatus(1);
            newJob.setJobType("听歌排行爬取任务");
            newJob.setTargetUserid(userRefJob.getTargetUserId());
            newJob.setTargetNickname(userRefJob.getTargetNickname());
            newJob.setCrtUser(userRefJob.getOpenId());
            newJob.setCrtTime(new Date());
            timerJobBiz.insert(newJob);
            baseQuartzBiz.createJobByGosTimerJob(newJob);
        }
        return super.add(userRefJob, bindingResult);
    }
}
