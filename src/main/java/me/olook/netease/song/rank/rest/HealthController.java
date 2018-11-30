package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyPoolUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhaohw
 * @date 2018-11-30 14:12
 */
@RestController
@RequestMapping("health")
@Api(description = "系统监控检查")
public class HealthController {

    @ApiOperation(value = "代理池状态")
    @GetMapping(value = "proxy")
    public ResponseEntity proxy(){
        Map<String,Object> map = new HashMap<>(5);
        map.put("activeSize",ProxyPoolUtil.activeSize());
        ConcurrentLinkedQueue<ProxyInfo> workQueue = ProxyPoolUtil.getWorkQueue();
        ConcurrentLinkedQueue<ProxyInfo> failQueue = ProxyPoolUtil.getFailQueue();
        map.put("workQueueSize",workQueue.size());
        map.put("failQueueSize",failQueue.size());
        map.put("workQueue",workQueue);
        map.put("failQueue",failQueue);
        return ResponseEntity.status(200).body(map);
    }
}
