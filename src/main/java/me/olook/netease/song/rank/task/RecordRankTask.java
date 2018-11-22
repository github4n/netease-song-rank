package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.core.RecordRankService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-13 15:19
 */
@Slf4j
@Component
public class RecordRankTask implements Job {

    @Resource
    private RecordRankService recordRankService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getTrigger().getJobKey();
        recordRankService.run(jobKey.getName());

    }


}
