package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.config.WxAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaohw
 * @date 2018-03-05 13:54
 */
@Controller
@RequestMapping("wx")
@Api(description = "微信服务器请求模块")
@EnableConfigurationProperties(WxAppProperties.class)
public class WxServerController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WxAppProperties wxApp;

    @ApiOperation(value = "获取openid")
    @RequestMapping(value = "getOpenid",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> add(String code){
        String params = "?appid=" + wxApp.getAppId() + "&secret=" + wxApp.getAppSecret() + "&js_code=" + code + "&grant_type=" + wxApp.getGrantType();
        String url = "https://api.weixin.qq.com/sns/jscode2session"+params;
        String json = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(json);
        return ResponseEntity.status(200).body(json);
    }
}
