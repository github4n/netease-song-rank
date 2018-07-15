package me.olook.netease.song.rank.task;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.biz.TemplateMessageBiz;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 模板消息过期清理任务
 * @author zhaohw
 * @date 2018-07-15 10:38
 */
@Slf4j
@TimerJobTypeName(value = "过期推送模板清理任务")
public class TemplateMessageTask implements Job {

    @Autowired
    private TemplateMessageBiz templateMessageBiz;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        templateMessageBiz.updateExpired();
        log.info("清理过期推送模板消息记录...");
    }
}
