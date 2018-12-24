package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.dto.NetEaseUserDTO;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.wechat.WeChatHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 14:47
 */
@RestController
@RequestMapping("wx")
@Api(description = "微信服务器请求模块")
public class AppletServerController {

    private final WeChatHttpClient weChatHttpClient;

    @Autowired
    public AppletServerController(UserRefJobBiz userRefJobBiz, TimerJobBiz timerJobBiz,
                                  SongRankDataDiffBiz songRankDataDiffBiz, WeChatHttpClient weChatHttpClient) {
        this.weChatHttpClient = weChatHttpClient;
    }

    @ApiOperation(value = "获取会话信息")
    @GetMapping(value = "session")
    public ResponseEntity<String> getWxUserInfo(String code) {
        String json = jsCodeToSession(code);
        return ResponseEntity.status(200).body(json);
    }

    private String jsCodeToSession(String code){
        return weChatHttpClient.jsCodeToSession(code);
    }

    @ApiOperation(value = "获取网易云用户")
    @GetMapping(value = "netease/user")
    public ResponseEntity getNeteaseUser(String keyWord,
                                         @RequestParam(defaultValue = "5") String limit,
                                         @RequestParam(defaultValue = "0") String offset) {
        List<NetEaseUserDTO> list = NetEaseHttpClient.searchUser(keyWord,limit,offset);
        return ResponseEntity.status(200).body(list);
    }


}
