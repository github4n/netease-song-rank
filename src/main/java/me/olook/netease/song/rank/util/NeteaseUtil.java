package me.olook.netease.song.rank.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 网易云工具
 *
 * @author zhaohw
 * @date 2018-03-05 21:36
 */
public class NeteaseUtil {

    private static Logger log = LoggerFactory.getLogger(NeteaseUtil.class);

    /**
     * 搜索网易云用户
     * @param keyWord
     * @return
     */
    public static List<NeteaseUserDTO> searchUser(String keyWord){
        String json = netEaseSearch(keyWord,"1002","5","0");
        JSONObject jsonObject = parseObject(json);
        List<NeteaseUserDTO> list = Lists.newArrayList();
        JSONArray array = jsonObject.getJSONObject("result").getJSONArray("userprofiles");
        for(int i = 0 ; i <array.size();i++){
            String nickName = JSONObject.parseObject(array.get(i).toString()).get("nickname").toString();
            String avatar =  JSONObject.parseObject(array.get(i).toString()).get("avatarUrl").toString();
            String userId = JSONObject.parseObject(array.get(i).toString()).get("userId").toString();
            NeteaseUserDTO dto = new NeteaseUserDTO(userId,avatar,nickName);
            list.add(dto);
        }
        System.out.println(JSONObject.toJSONString(list));
        return list;
    }

    public static String netEaseSearch(String key,String type,String limit,String offset){
        String url = "http://music.163.com/api/search/get/web";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        try {
            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            pairList.add(new BasicNameValuePair("s", key));
            pairList.add(new BasicNameValuePair("type", type));
            pairList.add(new BasicNameValuePair("limit", limit));
            pairList.add(new BasicNameValuePair("offset", offset));
            pairList.add(new BasicNameValuePair("total", "true"));
            request.setHeader(HttpHeaders.ACCEPT, "*/*");
            request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate,sdch");
            request.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,en-US;q=0.7,en;q=0.3");
            request.setHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            request.setHeader(HttpHeaders.HOST, "music.163.com");
            request.setHeader(HttpHeaders.REFERER, "http://music.163.com/");
            //request.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64; rv:39.0) Gecko/20100101 Firefox/39.0");
            request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());
            request.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            //判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                //获取响应实体
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
            return null;
        } catch (IOException e) {
            log.error(" 用户搜索请求失败"+e.getMessage());
            return null;
        }
    }

    public static String songRank(String userId){
        Map<String,String> map = Maps.newHashMap();
        map.put("type","1");
        map.put("limit","1000");
        map.put("offset","0");
        map.put("total","true");
        map.put("csrf_token","");
        map.put("uid",userId);
        String json = JSONObject.toJSONString(map);
        String params = NeteaseEncryptUtil.getUrlParams(json);
        String url = "http://music.163.com/weapi/v1/play/record"+params;


        RequestConfig.Builder builder = RequestConfig.custom();
        if(ProxyUtil.currentProxy==null){
            ProxyUtil.init();
        }else{
            HttpHost proxy = new HttpHost(ProxyUtil.currentProxy.getIp(), ProxyUtil.currentProxy.getPort(), "http");
            builder.setProxy(proxy);
        }
        RequestConfig proxyConfig = builder.build();

        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(proxyConfig).build();
        HttpPost request = new HttpPost(url);
        try {
            request.setHeader(HttpHeaders.ACCEPT, "*/*");
            request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate,sdch");
            request.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,en-US;q=0.7,en;q=0.3");
            request.setHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            request.setHeader(HttpHeaders.HOST, "music.163.com");
            request.setHeader(HttpHeaders.REFERER, "http://music.163.com/");
            //request.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64; rv:39.0) Gecko/20100101 Firefox/39.0");
            request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            //判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                //获取响应实体
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
            return null;
        } catch (IOException e) {
            log.error("歌曲排行数据请求失败"+e.getMessage());
            ProxyUtil.init();
            return null;
        }
    }

    public static void main(String[] args) {
        //获取听歌排行数据
       System.out.println(songRank("33255454"));
    }
}
