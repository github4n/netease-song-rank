package me.olook.netease.song.rank.biz;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.task.RecordRankTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * @author zhaohw
 * @date 2018-11-13 15:07
 */
@Slf4j
@Service
public class BaseQuartzBiz {

    private final SchedulerFactoryBean schedulerFactory;

    @Autowired
    public BaseQuartzBiz(SchedulerFactoryBean schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    public SchedulerFactoryBean getSchedulerFactory() {
        return schedulerFactory;
    }


    public void createScheduleJob(String jobName, String jobGroup,
                                  String cronExpression, Class<? extends Job> jobClass) {

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            log.debug("创建TimerJob: "+jobName+"_"+jobGroup);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void updateScheduleJob(String jobName, String jobGroup, String cronExpression) {
        try {
            TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) schedulerFactory.getScheduler().getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            schedulerFactory.getScheduler().rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteScheduleJob(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(getTriggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(getTriggerKey(jobName, jobGroup));
            scheduler.deleteJob(getJobKey(jobName, jobGroup));
            log.info("删除TimerJob: "+jobName+"_"+jobGroup);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            schedulerFactory.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resumeJob(String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            schedulerFactory.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    public static JobKey getJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }


    /**
     * 创建爬虫任务
     */
    public boolean createTimerJob(TimerJob timerJob){
        String jobType = timerJob.getJobType();
        createScheduleJob(timerJob.getJobName(),timerJob.getJobGroup(),
                timerJob.getCronExpression(), RecordRankTask.class);
        return true;
    }

}
