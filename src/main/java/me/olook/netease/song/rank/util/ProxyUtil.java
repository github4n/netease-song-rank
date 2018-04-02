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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhaohw
 * @date 2018-04-02 11:55
 */
public class ProxyUtil {

    private static Logger log = LoggerFactory.getLogger(ApplicationContextUtil.class);

    public volatile static ProxyInfo currentProxy = null;

    public synchronized static void init(){
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request =
                    new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=&num=1&delay=2&category=2&protocol=https&sortby=time&filter=on");
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    //获取响应实体
                   String res = EntityUtils.toString(response.getEntity(), "utf-8");
                   String[] proxys = res.split(System.getProperty("line.separator"));
                    for(String proxy : proxys){
                        if(proxy.split(":").length<2){
                            log.warn("代理格式不正确"+proxy);
                            init();
                        }
                        ProxyInfo proxyInfo = new ProxyInfo(proxy);
                        if(checkProxy(proxyInfo.getIp(),proxyInfo.getPort())){
                            currentProxy = proxyInfo;
                            log.info("获取到可用代理配置:"+proxyInfo.toString());
                        }else{
                            init();
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

    public static void main(String[] args) {
        init();
    }
}

class ProxyInfo {

    ProxyInfo(String proxy){
        this.ip = proxy.split(":")[0];
        this.port = Integer.parseInt(proxy.split(":")[1]);
    }

    private String ip;

    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ProxyInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}