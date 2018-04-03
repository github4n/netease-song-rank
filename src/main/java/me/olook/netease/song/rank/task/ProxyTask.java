package me.olook.netease.song.rank.task;

import com.alibaba.fastjson.JSONObject;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.util.ProxyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.olook.netease.song.rank.util.ProxyUtil.currentProxy;

/**
 * @author zhaohw
 * @date 2018-04-03 9:45
 */
//@DisallowConcurrentExecution
@TimerJobTypeName(value = "代理维护任务")
public class ProxyTask implements Job {

    private Logger log = LoggerFactory.getLogger(ProxyTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int size = currentProxy.size();

        log.info("当前存活有效代理数量: "+ currentProxy.size()+" 任务执行状态: "
                +ProxyUtil.isInit+" 当前正在填充: "+ProxyUtil.index
                +"\n"+ JSONObject.toJSONString(currentProxy));
        if(!ProxyUtil.isInit){
            log.info("开始执行代理维护任务...");
            for(int i = 1 ; i<10 ;i++){
                if(currentProxy.get(i)==null){
                    ProxyUtil.fixProxyPool(i);
                }
            }
            log.info("代理维护任务本次执行结束!");
        }
    }


}
