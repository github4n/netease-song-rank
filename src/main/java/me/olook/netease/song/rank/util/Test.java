package me.olook.netease.song.rank.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaohw
 * @date 2019-01-04 17:39
 */
public class Test {

    public static void main(String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpHost proxy = new HttpHost("127.0.0.1",8088);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(15000)
                .setSocketTimeout(15000)
                .setConnectionRequestTimeout(10000)
                .build();

            HttpGet request = new HttpGet("http://www.gatherproxy.com/zh/proxylist/country/?c=China");
            request.setConfig(requestConfig);
            try {
                HttpResponse response = httpClient.execute(request);
                String s = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
                Document parse = Jsoup.parse(s);
                Elements scripts = parse.select("script");
                List<JSONObject> collect = scripts.stream()
                        .filter(text -> text.data().contains("gp.insertPrx"))
                        .map(p -> p.data().replace("gp.insertPrx(", "").replace(");", "").trim())
                        .map(JSON::parseObject)
                        .collect(Collectors.toList());
                collect.forEach(json-> System.out.println(json.getString("PROXY_IP")+":"+Integer.parseInt(json.getString("PROXY_PORT"),16)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


}
