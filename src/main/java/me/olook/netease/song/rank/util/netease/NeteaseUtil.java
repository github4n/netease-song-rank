package me.olook.netease.song.rank.util.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.exception.PermissionDeniedException;
import me.olook.netease.song.rank.exception.ProxyInvalidException;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.UserAgents;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseObject;
import static me.olook.netease.song.rank.util.proxy.ProxyUtil.currentProxy;

/**
 * 网易云工具
 *
 * @author zhaohw
 * @date 2018-03-05 21:36
 */
@Slf4j
public class NeteaseUtil {

    /**
     * 搜索网易云用户
     * @param keyWord 关键词
     * @return 用户列表
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
            request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());
            request.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
            return null;
        } catch (IOException e) {
            log.error(" 用户搜索请求失败 {}",e.getMessage());
            return null;
        }
    }

    /**
     * 听歌排行请求
     * @param userId
     * @return
     */
    public static JSONObject songRank(String userId){

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

        Random random = new Random();
        int randomProxyKey = -1;
        ProxyInfo proxyInfo = null;

        //至少两个代理才使用代理池
        if(currentProxy.size() > 1){
            Set<Integer> keySet = currentProxy.keySet();
            Object[] keySetArray = keySet.toArray();
            int randomArrayIndex = random.nextInt(keySetArray.length);
            randomProxyKey = Integer.parseInt(keySetArray[randomArrayIndex].toString());
            proxyInfo = currentProxy.get(randomProxyKey);
            if(proxyInfo!=null){
                HttpHost proxy = new HttpHost(proxyInfo.getIp(), proxyInfo.getPort(), "http");
                builder.setProxy(proxy);
            }
        }
        builder.setConnectTimeout(5000);
        builder.setConnectionRequestTimeout(5000);
        builder.setSocketTimeout(5000);
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
            request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
                if(jsonStr == null) {
                    log.error("{} 获取排行数据失败",userId);
                    return null;
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                String code = jsonObject.get("code").toString();
                if("-460".equals(code)||"403".equals(code)){
                    if(proxyInfo != null){
                        log.info(userId+" code -406 "+proxyInfo.toString());
                        if(currentProxy.get(randomProxyKey)!=null){
                            log.error("{} 反爬虫执行 , 删除代理 {}",userId,randomProxyKey);
                            currentProxy.remove(randomProxyKey);
                        }
                    }else {
                        log.warn("{} 反爬虫执行 , 代理池为空",userId);
                        currentProxy.remove(randomProxyKey);
                    }
                    return null;
                }
                if("-2".equals(code)){
                    log.warn("{} 获取数据权限不足 code: {}",userId,code);
                    throw new PermissionDeniedException(userId+" 获取数据权限不足 , code: "+code);
                }
                if(!"200".equals(code)){
                    log.warn("{} 获取数据返回码错误 code: {}",userId,code);
                    log.warn(jsonStr);
                    return null;
                }

                log.debug("{} 获取到有效数据,使用代理 {}", userId,proxyInfo);
                return jsonObject;
            }
            log.warn("{} http返回码错误: {}",userId,response.getStatusLine().getStatusCode());
            if(currentProxy.get(randomProxyKey)!=null){
                log.warn("http返回码错误,移除失效代理 {}",randomProxyKey);
                currentProxy.remove(randomProxyKey);
            }
            return null;
        } catch (IOException e) {
            log.error("{} http IO异常,使用代理 {}",userId, proxyInfo);
            if(currentProxy.get(randomProxyKey)!=null){
                currentProxy.remove(randomProxyKey);
                throw new ProxyInvalidException(currentProxy.get(randomProxyKey)!=null?currentProxy.get(randomProxyKey).toString():"null proxy");
            }
            return null;
        }
    }

    /**
     * 获取听歌量请求
     * @param userId
     * @return 异常返回-1 正常返回听歌量
     */
    private static int songCount(String userId){
        String url = "http://music.163.com/user/songs/rank?id="+userId;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        try {
            request.setHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
            request.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
            request.setHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            request.setHeader(HttpHeaders.HOST, "music.163.com");
            request.setHeader(HttpHeaders.REFERER, "http://music.163.com/");
            request.setHeader(HttpHeaders.USER_AGENT,UserAgents.randomUserAgent());

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                String responseHtml = EntityUtils.toString(response.getEntity(), "utf-8");
                Document document = Jsoup.parse(responseHtml);
                Element element = document.select("h4").get(0);
                System.out.println(element.text());
                if(element.text().contains("累积听歌")){
                    return Integer.parseInt(element.text().replace("累积听歌","").replace("首",""));
                }
            }
            return -1;
        } catch (IOException e) {
            log.error(" 用户听歌数量获取失败 {}",e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        //获取听歌排行数据
       System.out.println(songRank("9753203"));
    }
}
