package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-01-10 17:46
 */
@Controller
@RequestMapping("timer")
@Api(description = "定时任务模块")
public class TimerJobController extends BaseController<TimerJobBiz,TimerJob>{

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseEntity<String> add(@RequestBody @Valid TimerJob timerJob, BindingResult bindingResult) {
        int result = baseBiz.insertSelective(timerJob);
        if(result==-1){
            return ResponseEntity.status(500).body("唯一键重复");
        }
        if(TimerJob.STATUS_RUN.equals(timerJob.getStatus())){
            baseQuartzBiz.createJobByGosTimerJob(timerJob);
        }
        return ResponseEntity.status(200).body("新增成功");
    }

    @ApiOperation(value = "通过id更新")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @Override
    public ResponseEntity<String> update(@PathVariable Object id, @RequestBody TimerJob timerJob) {
        try {
            ReflectionUtils.setPrimaryKey(timerJob,id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("主键查询失败");
        }
        TimerJob targetJob = baseBiz.selectById(Integer.parseInt(id.toString()));
        int num = baseBiz.updateSelectiveById(timerJob);
        if(!timerJob.getStatus().equals(targetJob.getStatus())){
            if(TimerJob.STATUS_RUN.equals(timerJob.getStatus())){
                baseQuartzBiz.createJobByGosTimerJob(timerJob);
            }else if(TimerJob.STATUS_STOP.equals(timerJob.getStatus())){
                baseQuartzBiz.deleteScheduleJob(targetJob.getJobName(),targetJob.getJobGroup());
            }
        }
        if(num > 0){
            return ResponseEntity.status(200).body("更新成功");
        }else{
            return ResponseEntity.status(500).body("更新数目为0");
        }
    }

    @ApiOperation(value = "通过id删除")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @Override
    public ResponseEntity<String> remove(@PathVariable Object id) {
        TimerJob targetJob = baseBiz.selectById(id);
        if(TimerJob.STATUS_RUN.equals(targetJob.getStatus())){
            baseQuartzBiz.deleteScheduleJob(targetJob.getJobName(),targetJob.getJobGroup());
        }
        return super.remove(id);
    }

    @ApiOperation(value = "清理不活跃任务")
    @RequestMapping(value = "clear",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity clear(Integer dayDiff){
        List<TimerJob> timerJobs = baseBiz.findExpiredTimerJob(dayDiff);
        timerJobs.forEach(p->{
            baseQuartzBiz.deleteScheduleJob(p.getJobName(),p.getJobGroup());
            p.setStatus(TimerJob.STATUS_EXPIRED);
            p.setUpdTime(new Date());
            p.setUpdName("不活跃手动清理");
            baseBiz.updateSelectiveById(p);
            userRefJobBiz.updateDelFlagByTargetUserId(p.getTargetUserid(),1);
        });
        return ResponseEntity.status(200).body("清理不活跃爬虫任务 "+timerJobs.size()+" 个");
    }
}
