package me.olook.netease.song.rank.task;

import com.alibaba.fastjson.JSONObject;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.util.proxy.ProxyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static me.olook.netease.song.rank.util.proxy.ProxyUtil.DEFAULT_PROXY_POOL_SIZE;
import static me.olook.netease.song.rank.util.proxy.ProxyUtil.currentProxy;

/**
 * @author zhaohw
 * @date 2018-04-03 9:45
 */
//@DisallowConcurrentExecution
@TimerJobTypeName(value = "代理维护任务")
public class ProxyTask implements Job {

    private Logger log = LoggerFactory.getLogger(ProxyTask.class);

    private static AtomicBoolean lock = new AtomicBoolean(false);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int size = currentProxy.size();
        log.info("当前存活有效代理数量: {} 任务执行状态: {} 当前正在填充: {}",currentProxy.size(),
                lock.get(),ProxyUtil.index);
        log.debug("{}", JSONObject.toJSONString(currentProxy));
        if(DEFAULT_PROXY_POOL_SIZE == size){
            log.info("代理池已满,无需维护...");
            return;
        }
        if(!lock.get()){
            log.info("开始执行代理维护任务...");
            lock.getAndSet(true);
            for(int i = 0; i< DEFAULT_PROXY_POOL_SIZE ; i++){
                if(currentProxy.get(i)==null){
                    ProxyUtil.fixProxyPool(i);
                }
            }
            log.info("代理维护任务本次执行结束!");
            lock.getAndSet(false);
        }
    }


}
