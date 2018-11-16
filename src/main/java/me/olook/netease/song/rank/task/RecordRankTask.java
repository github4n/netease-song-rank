package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author zhaohw
 * @date 2018-11-13 15:19
 */
@Slf4j
public class RecordRankTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //System.out.println( context.getTrigger().getJobKey());
    }
}
