package me.olook.netease.song.rank.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.config.WxAppProperties;
import me.olook.netease.song.rank.dto.TemplateMsgKeyWord;
import me.olook.netease.song.rank.dto.TemplateMsgParam;
import me.olook.netease.song.rank.entity.AccessTokenInfo;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TemplateMessage;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.mapper.TemplateMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhaohw
 * @date 2018-03-08 13:07
 */
@Service
public class TemplateMessageBiz extends BaseBiz<TemplateMessageMapper,TemplateMessage>{

    private Logger log = LoggerFactory.getLogger(TemplateMessageBiz.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessTokenInfoBiz accessTokenInfoBiz;

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @Autowired
    private WxAppProperties wxApp;

    /**
     * 推送模板消息
     * @param list 待推用户列表
     * @param dataDiff 歌曲变更信息
     */
    public void pushTemplateMsg(List<TemplateMessage> list, SongRankDataDiff dataDiff){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String validToken = getValidAccessToken();
        if(validToken==null){
            log.error("推送模板消息找不到可用 access token");
            return;
        }
        UserRefJob job = userRefJobBiz.getUserJobByTargetUserId(dataDiff.getTargetUserId());
        Map<String,Object> pushedMap = Maps.newHashMap();
        for(TemplateMessage msg : list){
            if(pushedMap.get(msg.getOpenid())!=null){
                continue;
            }
            TemplateMsgParam param = new TemplateMsgParam();
            param.setTouser(msg.getOpenid());
            param.setTemplate_id(msg.getTemplateId());
            param.setPage(msg.getPage());
            param.setForm_id(msg.getFormId());
            TemplateMsgKeyWord keyWord = new TemplateMsgKeyWord(dataDiff.getSong(),sdf.format(new Date()),job.getTargetNickname());
            param.setData(keyWord);
            sendPushRequest(param, validToken);
            msg.setIsValid(0);
            mapper.updateByPrimaryKeySelective(msg);
        }

    }

    /**
     * 推送一个模板消息
     * @param param
     * @param token
     */
    private void sendPushRequest(TemplateMsgParam param, String token ){
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+token;
        String paramJson = JSONObject.toJSONString(param);
        log.debug("模板消息参数: "+paramJson);
        HttpEntity<String> formEntity = new HttpEntity<String>(paramJson);
        String result = restTemplate.postForObject(url, formEntity, String.class);
        log.debug("模板消息响应: "+result);
    }


    /**
     * 获取可用的accessToken
     * @return
     */
    private String getValidAccessToken(){
        AccessTokenInfo accessToken = accessTokenInfoBiz.getLatestValidToken();
        String validToken = "";
        if(accessToken==null||accessToken.getInvalidTime().getTime()<System.currentTimeMillis()){
            //重新获取token
            String token = getAccessToken();
            if(token == null){
                log.error("获取 access token 失败");
                return null;
            }
            if(accessToken!=null){
                accessToken.setIsValid(0);
                accessTokenInfoBiz.updateSelectiveById(accessToken);
            }
            AccessTokenInfo newTokenInfo = new AccessTokenInfo(token);
            accessTokenInfoBiz.insert(newTokenInfo);
            validToken = token;
        }else {
            validToken = accessToken.getAccessToken();
        }
        return validToken;
    }

    /**
     * 获取accessToken
     * @return
     */
    private String getAccessToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wxApp.getAppId()+"&secret="+wxApp.getAppSecret();
        String wxJson = restTemplate.getForEntity(url, String.class).getBody();
        log.debug("access-token响应: "+wxJson);
        JSONObject jsonObject = JSON.parseObject(wxJson);
        Object accessTokenObj = jsonObject.get("access_token");
        if(accessTokenObj == null){
            return null;
        }
        return accessTokenObj.toString();
    }
}
