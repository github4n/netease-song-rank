package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 不活跃爬虫清理任务
 * @author zhaohw
 * @date 2018-07-18 13:16
 */
@Slf4j
@TimerJobTypeName(value = "不活跃爬虫清理任务")
public class TimerJobTask implements Job {

    /**
     * 不活跃判定周期
     */
    private final static int DAY_DIFF = 5;

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<TimerJob> timerJobs = timerJobBiz.findExpiredTimerJob(DAY_DIFF);
        timerJobs.forEach(p->{
            //删除quartz job
            baseQuartzBiz.deleteScheduleJob(p.getJobName(),p.getJobGroup());
            //更新timer job 状态
            p.setStatus(TimerJob.STATUS_EXPIRED);
            p.setUpdTime(new Date());
            p.setUpdName("不活跃自动清理");
            timerJobBiz.updateSelectiveById(p);
            //更新user_ref_job del_flag
            userRefJobBiz.updateDelFlagByTargetUserId(p.getTargetUserid(),1);
        });
        log.info("清理不活跃爬虫任务 {} 个 ...",timerJobs.size());
    }
}
