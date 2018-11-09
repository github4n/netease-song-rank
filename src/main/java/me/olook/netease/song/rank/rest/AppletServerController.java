package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.properties.WxAppProperties;
import me.olook.netease.song.rank.util.netease.NetEaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 14:47
 */
@RestController
@RequestMapping("wx")
@Api(description = "微信服务器请求模块")
@EnableConfigurationProperties(WxAppProperties.class)
public class AppletServerController {

    private final WxAppProperties wxAppProperties;

    private final RestTemplate restTemplate;

    private final UserRefJobBiz userRefJobBiz;

    @Autowired
    public AppletServerController(WxAppProperties wxAppProperties, RestTemplate restTemplate, UserRefJobBiz userRefJobBiz) {
        this.wxAppProperties = wxAppProperties;
        this.restTemplate = restTemplate;
        this.userRefJobBiz = userRefJobBiz;
    }

    @GetMapping(value = "session")
    public ResponseEntity<String> getWxUserInfo(String jsCode) {
        String json = jsCodeToSession(jsCode);
        return ResponseEntity.status(200).body(json);
    }

    // todo   整合微信请求工具类   zhaohw 2018/11/9 15:59

    private  String jsCodeToSession(String code){
        String params = "?appid=" + wxAppProperties.getAppId()
                + "&secret=" + wxAppProperties.getAppSecret()
                + "&js_code=" + code
                + "&grant_type=" + wxAppProperties.getGrantType();
        String url = "https://api.weixin.qq.com/sns/jscode2session" + params;
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    @ApiOperation(value = "获取用户关联的任务")
    @GetMapping(value = "jobs")
    public ResponseEntity getUerJob(String openid) {
        List<UserRefJob> jobList = userRefJobBiz.findByOpenIdAndDelFlag(openid,0);
        return ResponseEntity.status(200).body(jobList);
    }

    @ApiOperation(value = "获取网易云用户")
    @GetMapping(value = "netease/user")
    @ResponseBody
    public ResponseEntity getNeteaseUser(String keyWord,
                                         @RequestParam(defaultValue = "5") String limit,
                                         @RequestParam(defaultValue = "0") String offset) {
        List<NeteaseUserDTO> list = NetEaseUtil.searchUser(keyWord,limit,offset);
        return ResponseEntity.status(200).body(list);
    }
}
