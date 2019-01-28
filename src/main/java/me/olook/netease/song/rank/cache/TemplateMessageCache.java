package me.olook.netease.song.rank.cache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaohw
 * @date 2018-12-25 10:14
 */
public class TemplateMessageCache {

    private final static Long MAX_LIVE_DAY = 6L;

    private final static String CONNECTOR = "-";
    
    private static ConcurrentHashMap<String, LocalDateTime> cacheMap = new ConcurrentHashMap<>();

    
    private static void initCache(String key){
        cacheMap.put(key,LocalDateTime.now());
    }

    private static void initCache(String targetUserId,String openid){
        initCache(targetUserId+CONNECTOR+openid);
    }

    /**
     * 校验缓存
     * 是否满足推送条件
     */
    public static boolean checkCache(String targetUserId,String openid,Integer interval){
        if(interval == null){
            interval = 0;
        }
        String key = targetUserId+CONNECTOR+openid;
        if(!cacheMap.containsKey(key)){
            initCache(key);
            return true;
        }
        LocalDateTime lastTime = cacheMap.get(key);
        Duration duration = Duration.between(lastTime,LocalDateTime.now());
        long minutes = duration.toMinutes();
        return minutes > interval;
    }

    /**
     * 清理缓存
     * 删除超过最大存活天数的缓存
     */
    public static void cleanCache(){
        cacheMap.forEach((k,v)->{
            long days = Duration.between(LocalDateTime.now(),v).toDays();
            if(days > MAX_LIVE_DAY){
                cacheMap.remove(k);
            }
        });
    }

    public static ConcurrentHashMap info(){
        return cacheMap;
    }

}
