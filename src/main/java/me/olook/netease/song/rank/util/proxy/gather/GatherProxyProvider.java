package me.olook.netease.song.rank.util.proxy.gather;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.proxy.ProxyCheckJob;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
import me.olook.netease.song.rank.util.proxy.ProxyPool;
import me.olook.netease.song.rank.util.proxy.ProxyProvider;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaohw
 * @date 2019-01-11 16:53
 */
@Slf4j
public class GatherProxyProvider implements ProxyProvider {

    private HttpClient httpClient = HttpClientBuilder.create().build();

    private HttpHost proxy = new HttpHost("127.0.0.1",8081);

    private RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy)
            .setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000)
            .build();

    private Integer page = 1;

    @Override
    public String requestForPayload() {
        HttpPost request = new HttpPost("http://www.gatherproxy.com/proxylist/anonymity/?t=Elite");
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("Uptime", "0"));
        pairList.add(new BasicNameValuePair("Type", "elite"));
        pairList.add(new BasicNameValuePair("PageIdx", page.toString()));
        request.setConfig(requestConfig);
        request.setEntity(new UrlEncodedFormEntity(pairList, Charsets.UTF_8));
        try {
            HttpResponse response = httpClient.execute(request);
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
        Elements trs = parse.select("tr");
        List<Element> collect = trs.stream().filter(e -> {
            try{
                String script = e.childNode(3).childNode(0).nodeName();
                return "script".equals(script);
            }catch (Exception e1){
                return false;
            }
        }).collect(Collectors.toList());

        return collect.stream().map(e -> {
            String ip = e.child(1).data().replace("document.write('", "").replace("')", "");
            String portStr = e.child(2).data().replace("document.write(gp.dep('", "").replace("'))", "");
            Integer port = Integer.parseInt(portStr, 16);
            return new ProxyInfo(ip, port, LocalDateTime.now());
        }).collect(Collectors.toList());
    }

    @Override
    public void fixProxyPool() {
        for(int i = 1; i < 4; i++){
            this.page = i;
            String s = requestForPayload();
            List<ProxyInfo> proxyInfos = resolveProxy(s);
            log.info("request gather proxy list page : {}",this.page);
            for(ProxyInfo proxyInfo: proxyInfos){
                    ProxyPool.executors.submit(new ProxyCheckJob(proxyInfo));
            }
        }
    }

    public static void main(String[] args) {
        ProxyProvider proxyProvider = new GatherProxyProvider();
        proxyProvider.fixProxyPool();
    }
}
