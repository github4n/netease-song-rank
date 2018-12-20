package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-12-20 16:31
 */
@RestController
@RequestMapping("rank")
@Api(description = "听歌排行模块")
public class RecordRankController {

    private final TimerJobBiz timerJobBiz;

    private final SongRankDataDiffBiz songRankDataDiffBiz;

    @Autowired
    public RecordRankController(TimerJobBiz timerJobBiz, SongRankDataDiffBiz songRankDataDiffBiz) {
        this.timerJobBiz = timerJobBiz;
        this.songRankDataDiffBiz = songRankDataDiffBiz;
    }

    @ApiOperation(value = "测试能否获取排行数据")
    @GetMapping(value = "rank/check")
    public ResponseEntity checkSongRank(String targetUserId) {
        boolean result = NetEaseHttpClient.checkRankAccess(targetUserId);
        if (result) {
            return ResponseEntity.status(200).body(true);
        }
        return ResponseEntity.status(403).body(false);
    }

    @ApiOperation(value = "获取排行变化数据")
    @GetMapping(value = "rank/record")
    public ResponseEntity getSongRankRecord(String targetUserId) {
        TimerJob timerJob = timerJobBiz.findByTargetUserId(targetUserId);
        if(timerJob == null || timerJob.getStatus().equals(TimerJob.STATUS_STOP)){
            return ResponseEntity.status(404).body("出错啦!建议取消再重新关注Ta.");
        }
        if(timerJob.getStatus().equals(TimerJob.STATUS_EXPIRED)){
            return ResponseEntity.status(404).body("你的关注过期啦!建议取消再重新关注Ta.");
        }
        timerJob.setUpdTime(new Date());
        timerJobBiz.save(timerJob);
        //查出最近10条记录
        List<SongRankDataDiff> dataDiffs = songRankDataDiffBiz.findLatestRecordRank(targetUserId);
        return ResponseEntity.status(200).body(dataDiffs);
    }
}
