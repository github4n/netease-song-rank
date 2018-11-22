package me.olook.netease.song.rank.biz;

import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.entity.AccessTokenInfo;
import me.olook.netease.song.rank.repository.AccessTokenInfoRepository;
import me.olook.netease.song.rank.util.wechat.WeChatHttpClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaohw
 * @date 2018-11-08 17:01
 */
@Slf4j
@Service
public class AccessTokenInfoBiz {

    @Resource
    private AccessTokenInfoRepository accessTokenInfoRepository;

    @Resource
    private WeChatHttpClient weChatHttpClient;

    public AccessTokenInfo save(AccessTokenInfo accessTokenInfo){
        return accessTokenInfoRepository.save(accessTokenInfo);
    }

    /**
     * 获取可用的accessToken
     */
    public String getValidAccessToken(){
        AccessTokenInfo accessToken = this.getLatestValidToken();
        String validToken = "";
        if(accessToken==null||accessToken.getInvalidTime().getTime()<System.currentTimeMillis()){
            //重新获取token
            String token = weChatHttpClient.getAccessToken();
            if(token == null){
                log.error("获取 access token 失败");
                return null;
            }
            if(accessToken!=null){
                accessToken.setIsValid(0);
                this.save(accessToken);
            }
            AccessTokenInfo newTokenInfo = new AccessTokenInfo(token);
            this.save(newTokenInfo);
            validToken = token;
        }else {
            validToken = accessToken.getAccessToken();
        }
        return validToken;
    }

    private AccessTokenInfo getLatestValidToken(){
        return accessTokenInfoRepository.findFirstByIsValidOrderByCrtTimeDesc(1);
    }
}
