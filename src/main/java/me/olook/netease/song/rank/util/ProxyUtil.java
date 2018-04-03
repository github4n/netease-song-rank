package me.olook.netease.song.rank.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author zhaohw
 * @date 2018-04-02 11:55
 */
public class ProxyUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private static Logger log = LoggerFactory.getLogger(ApplicationContextUtil.class);

    public static Hashtable<Integer,ProxyInfo> currentProxy = new Hashtable<>();

    /**
     * 是否正在获取代理
     */
    public volatile static boolean isInit = false;

    public volatile static int index = 0;

    public static void init(int num){
                isInit = true;
                try {
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpGet request =
                            new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=订单号&num=1&delay=3&filter=on");
                    HttpResponse response = httpClient.execute(request);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                        //获取响应实体
                        String res = EntityUtils.toString(response.getEntity(), "utf-8");
                        String[] proxys = res.split(System.getProperty("line.separator"));
                        for(String proxy : proxys){
                            if(proxy.split(":").length<2){
                                //log.warn("代理格式不正确 "+proxy);
                                init(num);
                            }else{

                            String ip = proxy.split(":")[0];
                            Integer port = Integer.parseInt(proxy.split(":")[1]);
                            ProxyInfo proxyInfo = new ProxyInfo(ip,port);
                                if(checkProxy(proxyInfo.getIp(),proxyInfo.getPort())){
                                    currentProxy.put(num-currentProxy.size(),proxyInfo);
                                    log.info("添加可用代理配置:"+proxyInfo.toString());
                                    System.out.println(JSONObject.toJSONString(currentProxy));
                                    if(currentProxy.size()<num){
                                        init(num);
                                    }else{
                                        isInit =false;
                                    }
                                }else{
                                    init(num);
                                }
                            }
                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
        }


    private static boolean checkProxy(String ip,Integer port){
            log.info("校验代理: "+ip+":"+port);
            Map<String,String> map = Maps.newHashMap();
            map.put("type","1");
            map.put("limit","1000");
            map.put("offset","0");
            map.put("total","true");
            map.put("csrf_token","");
            map.put("uid","33255454");
            String json = JSONObject.toJSONString(map);
            String params = NeteaseEncryptUtil.getUrlParams(json);
            String url = "http://music.163.com/weapi/v1/play/record"+params;


            RequestConfig.Builder builder = RequestConfig.custom();
            HttpHost proxy = new HttpHost(ip, port, "http");
            builder.setProxy(proxy);
            builder.setConnectTimeout(2000);
            builder.setConnectionRequestTimeout(2000);
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
                // 通过请求对象获取响应对象
                HttpResponse response = httpClient.execute(request);
                //判断网络连接状态码是否正常(0--200都数正常)
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    //获取响应实体
                    return true;
                }
                return false;
            } catch (IOException e) {
                return false;
            }
        }

    public static void fixProxyPool(Integer key){
        index = key;
        isInit =true;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request =
                    new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=订单号&num=1&delay=3&filter=on");
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                //获取响应实体
                String res = EntityUtils.toString(response.getEntity(), "utf-8");
                String[] proxys = res.split(System.getProperty("line.separator"));
                for(String proxy : proxys){
                    if(proxy.split(":").length<2){
                        try {
                            log.warn(proxy);
                            Thread.sleep(3000);
                            fixProxyPool(key);
                        } catch (InterruptedException e) {
                            fixProxyPool(key);
                        }
                    }else {
                        String ip = proxy.split(":")[0];
                        Integer port = Integer.parseInt(proxy.split(":")[1]);

                        ProxyInfo proxyInfo = new ProxyInfo(ip,port);
                        if(checkProxy(proxyInfo.getIp(),proxyInfo.getPort())){
                            currentProxy.put(key,proxyInfo);
                            log.info("补充可用代理配置:"+key+"."+proxyInfo.toString());
                            isInit =false;
                        }else{
                            fixProxyPool(key);
                        }
                    }
                }
            }else{
                    fixProxyPool(key);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            ProxyUtil.init(5);
    }
}
