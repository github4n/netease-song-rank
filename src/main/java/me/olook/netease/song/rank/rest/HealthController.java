package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.cache.TemplateMessageCache;
import me.olook.netease.song.rank.util.proxy.ProxyPool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaohw
 * @date 2018-11-30 14:12
 */
@RestController
@RequestMapping("health")
@Api(description = "系统监控检查")
public class HealthController {

    @ApiOperation(value = "代理池状态")
    @GetMapping(value = "proxy/info")
    public ResponseEntity proxy(){
        Map<String,Object> map = new HashMap<>(5);
        map.put("activeSize",ProxyPool.activeSize());
        return ResponseEntity.status(200).body(map);
    }

    @ApiOperation(value = "推送缓存状态")
    @GetMapping(value = "cache/info")
    public ResponseEntity cacheInfo(){
        return ResponseEntity.status(200).body(TemplateMessageCache.info());
    }

    @ApiOperation(value = "清除推送缓存")
    @DeleteMapping(value = "cache/clean")
    public ResponseEntity cacheClean(){
        TemplateMessageCache.cleanCache();
        return ResponseEntity.status(200).body("");
    }
}
