package me.olook.netease.song.rank.util.proxy.gather;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.netease.NetEaseHttpClient;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyProvider;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaohw
 * @date 2019-01-11 16:53
 */
@Slf4j
public class GatherProxyGetProvider implements ProxyProvider {

    private HttpClient httpClient = HttpClientBuilder.create().build();

    private HttpHost proxy = new HttpHost("127.0.0.1",8088);

    private RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy)
            .setConnectTimeout(3000).setSocketTimeout(3000).setConnectionRequestTimeout(3000)
            .build();

    @Override
    public String requestForPayload() {
        HttpGet request = new HttpGet("http://www.gatherproxy.com/proxylist/anonymity/?t=Elite");
        request.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(request);
            System.out.println("");
            return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ProxyInfo> resolveProxy(String payload) {
        if(payload == null){
            return Collections.emptyList();
        }
        Document parse = Jsoup.parse(payload);
        Elements scripts = parse.select("script");
        List<JSONObject> collect = scripts.stream()
                .filter(text -> text.data().contains("gp.insertPrx"))
                .map(p -> p.data().replace("gp.insertPrx(", "").replace(");", "").trim())
                .map(JSON::parseObject)
                .collect(Collectors.toList());
        List<ProxyInfo> list = collect.stream()
                .map(json -> {
                    return new ProxyInfo(json.getString("PROXY_IP"), Integer.parseInt(json.getString("PROXY_PORT"), 16), LocalDateTime.now());
                })
                .collect(Collectors.toList());
        collect.forEach(json-> {
        });
        return list;
    }

    @Override
    public void fixProxyPool() {

    }

    public static void main(String[] args) {
        ProxyProvider proxyProvider = new GatherProxyGetProvider();
        String s = proxyProvider.requestForPayload();
        List<ProxyInfo> proxy = proxyProvider.resolveProxy(s);
        proxy.forEach(p->{
            boolean b = NetEaseHttpClient.checkProxy(p.getIp(), p.getPort());
            if(b) System.out.println(p);
        });
    }
}
