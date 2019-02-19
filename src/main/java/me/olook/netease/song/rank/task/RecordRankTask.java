package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.core.RecordRankService;
import me.olook.netease.song.rank.util.cache.TaskCacheUtil;
import org.quartz.*;
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
        try{
            JobKey jobKey = context.getTrigger().getJobKey();
            TaskCacheUtil.setStartTime(jobKey.getName());
            recordRankService.run(jobKey.getName());
        }catch (Exception e){
            JobExecutionException e2 = new JobExecutionException(e);
            e2.setRefireImmediately(true);
            throw e2;
        }

    }


}
