package me.olook.netease.song.rank.util.netease;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.util.proxy.UserAgents;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 网易云工具
 * @author zhaohw
 * @date 2018-03-05 21:36
 */
@Slf4j
public class NetEaseUtil {

    /**
     * 搜索网易云用户
     * @param keyWord 关键词
     * @return 用户列表
     */
    public static List<NeteaseUserDTO> searchUser(String keyWord,String limit,String offset){
        String json = netEaseSearch(keyWord,"1002",limit,offset);
        JSONObject jsonObject = JSON.parseObject(json);
        List<NeteaseUserDTO> list = Lists.newArrayList();
        JSONArray array = jsonObject.getJSONObject("result").getJSONArray("userprofiles");
        for (Object anArray : array) {
            list.add(resolveUser(anArray.toString()));
        }
        log.debug("用户搜素: {}",JSONObject.toJSONString(list));
        return list;
    }

    /**
     * 网易云搜索请求
     * @param key 关键词
     * @param type 搜索类型 1002 用户
     * @param limit 限制
     * @param offset 偏移
     * @return
     */
    private static String netEaseSearch(String key,String type,String limit,String offset){
        Map map = new HashMap<String,String>(4);
        map.put("s",key);
        map.put("type",type);
        map.put("limit",limit);
        map.put("offset",offset);
        map.put("total","true");
        return post(NeteaseApiUrl.SEARCH,map);
    }

    private static String post(String url , Map<String,String> params){
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        addNetEaseHeader(request);
        request.setEntity(new UrlEncodedFormEntity(addPostParam(params), Charsets.UTF_8));
        try {
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return EntityUtils.toString(response.getEntity(),Charsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("post IOException for {} ",url);
        }
        return null;
    }

    private static List<BasicNameValuePair> addPostParam(Map<String,String> params){
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        params.forEach((key, value) ->{
            pairList.add(new BasicNameValuePair(key, value));
        });
        return pairList;
    }

    private static void addNetEaseHeader(HttpRequestBase request){
        request.setHeader(HttpHeaders.ACCEPT, "*/*");
        request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate,sdch");
        request.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,en-US;q=0.7,en;q=0.3");
        request.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        request.setHeader(HttpHeaders.HOST, "music.163.com");
        request.setHeader(HttpHeaders.REFERER, "http://music.163.com/");
        request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());
    }

    private static NeteaseUserDTO resolveUser(String json){
        String nickName = JSONObject.parseObject(json).get("nickname").toString();
        String avatar = JSONObject.parseObject(json).get("avatarUrl").toString();
        String userId = JSONObject.parseObject(json).get("userId").toString();
        return new NeteaseUserDTO(userId, avatar, nickName);
    }
}
