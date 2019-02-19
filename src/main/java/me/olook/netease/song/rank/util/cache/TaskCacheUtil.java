package me.olook.netease.song.rank.util.cache;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaohw
 * @date 2019-02-18 17:42
 */
public class TaskCacheUtil {

    public static ConcurrentHashMap<String,LocalDateTime> startTime = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,LocalDateTime> endTime = new ConcurrentHashMap<>();

    public static void setStartTime(String id){
        startTime.put(id,LocalDateTime.now());
    }

    public static void setEndTime(String id){
        endTime.put(id,LocalDateTime.now());
    }

    public static Set<String> findException(){
        Set<String> ids = new HashSet<String>();
        startTime.forEach((k,v)->{
            if(!endTime.containsKey(k)||endTime.get(k).isBefore(v)){
                ids.add(k);
            }
        });
        return ids;
    }

    public static LocalDateTime getStartTime(String id){
        return startTime.get(id);
    }

    public static LocalDateTime getEndTime(String id){
        return endTime.get(id);
    }

    public static ConcurrentHashMap getStartTime(){
        return startTime;
    }

    public static ConcurrentHashMap getEndTime(){
        return endTime;
    }
}
