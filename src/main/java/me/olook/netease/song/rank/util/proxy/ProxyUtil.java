package me.olook.netease.song.rank.util.proxy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.netease.NeteaseEncryptUtil;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaohw
 * @date 2018-04-02 11:55
 */
@Slf4j
public class ProxyUtil {

    public static Hashtable<Integer,ProxyInfo> currentProxy = new Hashtable<>();

    public static Integer DEFAULT_PROXY_POOL_SIZE = 10;

    public static String PROXY_ORDER_ID ="";

    public static String PROXY_API_OPTION ="";
    /**
     * 是否正在获取代理
     */

    public static AtomicInteger index = new AtomicInteger(0);

    /**
     * 初始化代理池
     * @param num 代理池大小
     */
    public static void init(int num){
                try {
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpGet request =
                            new HttpGet("http://tvp.daxiangdaili.com/ip/?tid="+PROXY_ORDER_ID+PROXY_API_OPTION);
                    HttpResponse response = httpClient.execute(request);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                        //获取响应实体
                        String res = EntityUtils.toString(response.getEntity(), "utf-8");
                        String[] proxys = res.split(System.getProperty("line.separator"));
                        for(String proxy : proxys){
                            if(proxy.split(":").length<2){
                                //代理格式不正确，重新获取
                                init(num);
                            }else{

                            String ip = proxy.split(":")[0];
                            Integer port = Integer.parseInt(proxy.split(":")[1]);
                            ProxyInfo proxyInfo = new ProxyInfo(ip,port);
                                if(checkProxy(proxyInfo.getIp(),proxyInfo.getPort())){
                                    currentProxy.put(num-currentProxy.size(),proxyInfo);
                                    log.info("添加可用代理配置: {}",proxyInfo.toString());
                                    System.out.println(JSONObject.toJSONString(currentProxy));
                                    if(currentProxy.size()<num){
                                        init(num);
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
            log.debug("校验代理: {} : {}",ip,port);
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
            builder.setConnectTimeout(3000);
            builder.setConnectionRequestTimeout(3000);
            builder.setSocketTimeout(4000);
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
                return response.getStatusLine().getStatusCode() == HttpStatus.OK.value();
            } catch (IOException e) {
                return false;
            }
        }

    /**
     * 补充代理池代理
     * @param key 代理序号
     */
    public static void fixProxyPool(Integer key){
        index.getAndSet(key);
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request =
                    new HttpGet("http://tvp.daxiangdaili.com/ip/?tid="+PROXY_ORDER_ID+PROXY_API_OPTION);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                //获取响应实体
                String res = EntityUtils.toString(response.getEntity(), "utf-8");
                String[] proxys = res.split(System.getProperty("line.separator"));
                for(String proxy : proxys){
                    if(proxy.split(":").length<2){
                            log.warn(proxy);
                            Thread.sleep(3000);
                            fixProxyPool(key);
                    }else {
                        String ip = proxy.split(":")[0];
                        Integer port = Integer.parseInt(proxy.split(":")[1]);

                        ProxyInfo proxyInfo = new ProxyInfo(ip,port);
                        if(checkProxy(proxyInfo.getIp(),proxyInfo.getPort())){
                            currentProxy.put(key,proxyInfo);
                            log.info("accept proxy: {}. {}",key,proxyInfo.toString());
                        }else{
                            log.warn("discard proxy: {}. {}",key,proxyInfo.toString());
                            Thread.sleep(500);
                            fixProxyPool(key);
                        }
                    }
                }
            }else{
                Thread.sleep(500);
                fixProxyPool(key);
            }
        }catch (IOException | InterruptedException e) {
            log.error("fixProxyPool error , retry ... {} ",e.getMessage());
            fixProxyPool(key);
        }
    }

    public static void main(String[] args) {
            ProxyUtil.init(5);
    }
}
