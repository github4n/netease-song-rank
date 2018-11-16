package me.olook.netease.song.rank.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.dto.TemplateMsgParam;
import me.olook.netease.song.rank.properties.WxAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-16 11:35
 */
@Slf4j
@Component
@EnableConfigurationProperties(WxAppProperties.class)
public class WeChatHttpClient {

    @Resource
    private WxAppProperties wxAppProperties;

    @Resource
    private RestTemplate restTemplate;

    public String getAccessToken(){
        String url = WeChatApiUrl.ACCESS_TOKEN+"" +
                        "grant_type=client_credential" +
                        "&appid="+wxAppProperties.getAppId()+"" +
                        "&secret="+wxAppProperties.getAppSecret();
        String wxJson = restTemplate.getForEntity(url, String.class).getBody();
        log.info("access-token 响应: {}",wxJson);
        JSONObject jsonObject = JSON.parseObject(wxJson);
        return jsonObject.getString("access_token");
    }

    public String jsCodeToSession(String code){
        String params = "?appid=" + wxAppProperties.getAppId()
                + "&secret=" + wxAppProperties.getAppSecret()
                + "&js_code=" + code
                + "&grant_type=" + wxAppProperties.getGrantType();
        String url = WeChatApiUrl.JS_CODE_TO_SESSION + params;
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    public String sendPushTemplate(TemplateMsgParam param, String accessToken){
        String paramJson = JSONObject.toJSONString(param);
        log.info("模板消息参数: {}",paramJson);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(paramJson,headers);
        String result = restTemplate.postForObject(WeChatApiUrl.SEND_TEMPLATE+"?access_token="+accessToken,
                formEntity, String.class);
        log.info("模板消息响应: {}",result);
        return result;
    }
}
