package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.util.ReflectionUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 定时器工具
 * 封装定时器一些操作方法
 * @author zhaohw
 * @date 2017-11-08 15:22
 */
@Service
public class BaseQuartzBiz {

    private static Logger log = LoggerFactory.getLogger(BaseQuartzBiz.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    public SchedulerFactoryBean getSchedulerFactory() {
        return schedulerFactory;
    }


    public void createScheduleJob(String jobName, String jobGroup,
                                   String cronExpression, Class<? extends Job> jobClass) {

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup).build();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(cronExpression);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void updateScheduleJob(String jobName, String jobGroup, String cronExpression) {
        try {
            TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) schedulerFactory.getScheduler().getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
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
     * 通过注解 @TimerJobName 查找任务类
     * @return
     */
    public static Class<? extends Job> getJobClassByAnnotation(String value){
        List<Class<?>> jobClassList = ReflectionUtils.getClasses("me.olook.netease.song.rank.task");
        for(Class clazz : jobClassList){
            if (clazz.getAnnotation(TimerJobTypeName.class) != null && Job.class.isAssignableFrom(clazz)) {
                Annotation annotation = clazz.getAnnotation(TimerJobTypeName.class);
                TimerJobTypeName timerJobName = (TimerJobTypeName)annotation;
                if(value.equals(timerJobName.value())){
                    return clazz;
                }
            }

        }
        return null;
    }

    /**
     * 通过timerJob定义创建任务
     * @param timerJob
     * @return
     */
    public boolean createJobByGosTimerJob(TimerJob timerJob){
        String jobType = timerJob.getJobType();
        Class<? extends Job> taskClazz = getJobClassByAnnotation(jobType);
        if(taskClazz!=null){
            createScheduleJob(timerJob.getJobName(),timerJob.getJobGroup(),
                    timerJob.getCronExpression(), taskClazz);
            log.info("创建定时任务:"+timerJob.getJobName()+" 成功");
            return true;
        }else{
            log.error("任务名:"+timerJob.getJobName()+" 未找到注解TimerJobType为:"+jobType+" 的Job Class，任务创建失败");
            return false;
        }
    }
}
