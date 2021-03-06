package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.repository.SongRankDataDiffRepository;
import me.olook.netease.song.rank.repository.TimerJobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaohw
 * @date 2018-11-13 14:52
 */
@Service
public class SongRankDataDiffBiz {

    @Resource
    private TimerJobRepository timerJobRepository;

    @Resource
    private SongRankDataDiffRepository diffRepository;

    public List<SongRankDataDiff> findLatestRecordRank(String targetUserId){
        Pageable pageable = PageRequest.of(0,10);
        Page<SongRankDataDiff> recordRanks =
                diffRepository.findByTargetUserIdOrderByChangeTimeDesc(targetUserId, pageable);
        return recordRanks.getContent();
    }

    public SongRankDataDiff save(SongRankDataDiff songRankDataDiff){
        return diffRepository.save(songRankDataDiff);
    }

    /**
     * 找出排行变化的数据
     */
    public List<SongRankDataDiff> findSongRankDataDiffByRank(List<SongRankData> newList, List<SongRankData> oldList){
        Map<Integer,SongRankData> oldMap = new HashMap<Integer, SongRankData>(oldList.size());
        oldList.forEach(o->{
            oldMap.put(o.getSongId(),o);
        });
        List<SongRankDataDiff> diffList = new ArrayList<>();
        newList.forEach(n->{
            if(!oldMap.containsKey(n.getSongId())){
                SongRankDataDiff songRankDataDiff = buildDataDiff(n);
                diffList.add(songRankDataDiff);
            }
            else if(n.getRank()<oldMap.get(n.getSongId()).getRank()){
                SongRankDataDiff songRankDataDiff = buildDataDiff(n,oldMap.get(n.getSongId()).getRank()-n.getRank());
                diffList.add(songRankDataDiff);
            }
        });
        return diffList;
    }

    /**
     * 找出score变化的数据
     */
    public List<SongRankDataDiff> findSongRankDataDiffByScore(List<SongRankData> newList, List<SongRankData> oldList){
        Map<Integer,SongRankData> oldMap = new HashMap<Integer, SongRankData>(oldList.size());
        List<SongRankDataDiff> diffList = new ArrayList<>();
        oldList.forEach(o->{
            oldMap.put(o.getSongId(),o);
        });
        newList.forEach(n->{
            if(oldMap.containsKey(n.getSongId())){
                Integer newRatio = Integer.parseInt(n.getRatio());
                Integer oldRatio = Integer.parseInt(oldMap.get(n.getSongId()).getRatio());
                if(newRatio>oldRatio){
                    SongRankDataDiff songRankDataDiff = buildDataDiff(n,0);
                    diffList.add(songRankDataDiff);
                }
            }
        });
        return diffList;
    }

    private SongRankDataDiff buildDataDiff(SongRankData songRankData){
        return this.buildDataDiff(songRankData,-1);
    }

    private SongRankDataDiff buildDataDiff(SongRankData songRankData,Integer rankChange){
        return SongRankDataDiff.builder()
                .songId(songRankData.getSongId())
                .song(songRankData.getSong())
                .singer(songRankData.getSinger())
                .picUrl(songRankData.getPicUrl())
                .rankChange(rankChange)
                .build();
    }

    public List<SongRankDataDiff> findByGroup(String jobGroup, Pageable pageable){
        List<TimerJob> timerJobs = timerJobRepository.findByJobGroupAndStatusOrderByUpdTimeDesc(jobGroup, TimerJob.STATUS_RUN);
        List<String> targetUserIds = timerJobs.stream().map(TimerJob::getTargetUserId).collect(Collectors.toList());
        HashMap<String,String> idNameMap = new HashMap<>(timerJobs.size());
        timerJobs.forEach(p->{
            idNameMap.put(p.getTargetUserId(),p.getTargetNickname());
        });
        Page<SongRankDataDiff> diffs = diffRepository.findByTargetUserIdInOrderByChangeTimeDesc(targetUserIds, pageable);
        List<SongRankDataDiff> reduceList = diffs.getContent();
        reduceList.forEach(diff->{
            diff.setTargetUserName(idNameMap.get(diff.getTargetUserId()));
        });
        return reduceList;
    }
}
