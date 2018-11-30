package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.wechat.WeChatHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 14:47
 */
@RestController
@RequestMapping("wx")
@Api(description = "微信服务器请求模块")
public class AppletServerController {

    private final UserRefJobBiz userRefJobBiz;

    private final TimerJobBiz timerJobBiz;

    private final SongRankDataDiffBiz songRankDataDiffBiz;

    private final WeChatHttpClient weChatHttpClient;

    @Autowired
    public AppletServerController(UserRefJobBiz userRefJobBiz, TimerJobBiz timerJobBiz,
                                  SongRankDataDiffBiz songRankDataDiffBiz, WeChatHttpClient weChatHttpClient) {
        this.userRefJobBiz = userRefJobBiz;
        this.timerJobBiz = timerJobBiz;
        this.songRankDataDiffBiz = songRankDataDiffBiz;
        this.weChatHttpClient = weChatHttpClient;
    }

    @GetMapping(value = "session")
    public ResponseEntity<String> getWxUserInfo(String jsCode) {
        String json = jsCodeToSession(jsCode);
        return ResponseEntity.status(200).body(json);
    }

    private String jsCodeToSession(String code){
        return weChatHttpClient.jsCodeToSession(code);
    }

    @ApiOperation(value = "获取用户关联的任务")
    @GetMapping(value = "jobs")
    public ResponseEntity getUerJob(String openid) {
        List<UserRefJob> jobList = userRefJobBiz.findByOpenIdAndDelFlag(openid,0);
        return ResponseEntity.status(200).body(jobList);
    }

    @ApiOperation(value = "获取网易云用户")
    @GetMapping(value = "netease/user")
    public ResponseEntity getNeteaseUser(String keyWord,
                                         @RequestParam(defaultValue = "5") String limit,
                                         @RequestParam(defaultValue = "0") String offset) {
        List<NeteaseUserDTO> list = NetEaseHttpClient.searchUser(keyWord,limit,offset);
        return ResponseEntity.status(200).body(list);
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
