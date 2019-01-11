package me.olook.netease.song.rank.util.proxy.gather;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.util.proxy.ProxyInfo;
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
import org.jsoup.select.Elements;

import java.io.IOException;
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

    private HttpHost proxy = new HttpHost("127.0.0.1",8088);

    private RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy)
            .setConnectTimeout(3000).setSocketTimeout(3000).setConnectionRequestTimeout(3000)
            .build();

    @Override
    public String requestForPayload() {
        HttpPost request = new HttpPost("http://www.gatherproxy.com/proxylist/anonymity/?t=Elite");
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("Uptime", "0"));
        pairList.add(new BasicNameValuePair("Type", "elite"));
        pairList.add(new BasicNameValuePair("PageIdx", "1"));
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
        trs.stream().filter(e->{
            // todo 筛选 zhaohw 2019-01-11 17:56
            return e.childNodeSize()==16;
        }).collect(Collectors.toList());
        return null;
    }

    public static void main(String[] args) {
        ProxyProvider proxyProvider = new GatherProxyProvider();
        String s = proxyProvider.requestForPayload();
        proxyProvider.resolveProxy(s);
        System.out.println(s);
    }
}
