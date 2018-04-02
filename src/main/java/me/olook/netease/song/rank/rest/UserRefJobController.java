package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.biz.WxUserBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 每个用户默认可创建任务数量
     */
    private final static Integer DEFAULT_JOB_NUM = 3;

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Autowired
    private WxUserBiz wxUserBiz;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseEntity<String> add(@RequestBody @Valid UserRefJob userRefJob, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return ResponseEntity.status(500).body(errorList.get(0).getDefaultMessage());
        }
        synchronized (this){
            //该用户已添加的任务
            Example openIdExample = new Example(UserRefJob.class);
            openIdExample.createCriteria().andEqualTo("openId",userRefJob.getOpenId()).andEqualTo("delFlag",0);
            List<UserRefJob> listByOpenid = baseBiz.selectByExample(openIdExample);
            int jobLimit = DEFAULT_JOB_NUM;
            Example wxUserExm = new Example(WxUser.class);
            wxUserExm.createCriteria().andEqualTo("openId",userRefJob.getOpenId());
            List<WxUser> users = wxUserBiz.selectByExample(wxUserExm);
            if(users.size()>0){
                jobLimit = users.get(0).getLimit();
            }
            //每个用户当前只能创建3个任务
            if(listByOpenid.size()>=jobLimit){
                return ResponseEntity.status(401).body("您可关注好友数量已达上限!");
            }

            for(UserRefJob existJob : listByOpenid){
                if(existJob.getTargetUserId().equals(userRefJob.getOpenId())){
                    return ResponseEntity.status(401).body("您已经订阅过Ta了~");
                }
            }
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
            //已创建目标任务对象 未启动
            else if(TimerJob.STATUS_STOP.equals(timerJobs.get(0).getStatus())){
                    TimerJob oldTimerJob = timerJobs.get(0);
                    oldTimerJob.setStatus(TimerJob.STATUS_RUN);
                    timerJobBiz.updateSelectiveById(oldTimerJob);
                    baseQuartzBiz.createJobByGosTimerJob(oldTimerJob);

            }
            userRefJob.setCrtTime(new Date());
            int result = baseBiz.insertSelective(userRefJob);
        }
        //当前目标用户存在的任务数
        Example targetUserIdExample = new Example(UserRefJob.class);
        targetUserIdExample.createCriteria()
                .andEqualTo("targetUserId",userRefJob.getTargetUserId())
                .andEqualTo("delFlag",0);
        List<UserRefJob> listByTargetUserId = baseBiz.selectByExample(targetUserIdExample);

        return ResponseEntity.status(200).body("新增成功，您是第"+listByTargetUserId.size()+"位订阅Ta的好友!");
    }

    @Override
    @ApiOperation(value = "取消关注用户")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> remove(@PathVariable Object id){
        int intId  = Integer.parseInt(id.toString());
        UserRefJob userRefJob = baseBiz.selectById(intId);
        //查找是否用其他用户也在关注此人
        Example example = new Example(UserRefJob.class);
        example.createCriteria()
                .andEqualTo("targetUserId",userRefJob.getTargetUserId())
                .andEqualTo("delFlag",0);;
        List<UserRefJob> list = baseBiz.selectByExample(example);
        //只有待删除的这一条记录 删除job
        if(list.size()==1){
            Example jobExample = new Example(TimerJob.class);
            jobExample.createCriteria().andEqualTo("targetUserid",userRefJob.getTargetUserId());
            List<TimerJob> jobs = timerJobBiz.selectByExample(jobExample);
            if(jobs.size()>0){
                TimerJob job = jobs.get(0);
                job.setStatus(TimerJob.STATUS_STOP);
                timerJobBiz.updateSelectiveById(job);
                baseQuartzBiz.deleteScheduleJob(job.getJobName(),job.getJobGroup());
            }
        }
        //逻辑删
        userRefJob.setDelFlag(1);
        userRefJob.setUpdTime(new Date());
        int num = baseBiz.updateSelectiveById(userRefJob);
        if(num==0){
            return ResponseEntity.status(500).body("删除失败");
        }else{
            return ResponseEntity.status(200).body("删除成功");
        }
    }
}
