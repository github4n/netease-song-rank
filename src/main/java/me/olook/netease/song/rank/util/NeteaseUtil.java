package me.olook.netease.song.rank.util;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Lists;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 网易云工具
 *
 * @author zhaohw
 * @date 2018-03-05 21:36
 */
public class NeteaseUtil {

    /**
     * 搜索网易云用户
     * @param keyWord
     * @return
     */
    public static List<NeteaseUserDTO> searchUser(String keyWord){
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setRedirectEnabled(true);
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setActiveXNative(false);
        // wc.getOptions().setTimeout(100000);
        wc.getOptions().setDoNotTrackEnabled(false);
        String s ="";
        List<NeteaseUserDTO> userList = Lists.newArrayList();
        try {
            s = URLEncoder.encode(keyWord,"UTF-8");
            System.out.println(new Date());
        HtmlPage page = wc
                .getPage("http://music.163.com/#/search/m/?s="+s+"&type=1002");
            System.out.println(new Date());
        HtmlPage framePage = (HtmlPage)page.getFrameByName("contentFrame").getEnclosedPage();
            System.out.println(new Date());
        //累计听歌
        Document dom = Jsoup.parse(framePage.asXml());
        Elements avatars = dom.select("div.u-cover-3");

        int count = 0;
        for(Element e : avatars){
            String userId = e.select("a").attr("href").replace("/user/home?id=","");
            String img = e.select("img").attr("src");
            String nickname = e.select("span.msk").attr("title");
            NeteaseUserDTO dto = new NeteaseUserDTO(userId,img,nickname);
            userList.add(dto);
            count++;
            if(count>=5){
                break;
            }
        }
        }catch (IOException e){
            e.printStackTrace();
        }
        return userList;
    }

    public static void main(String[] args) {
        List<NeteaseUserDTO> list = searchUser("半赫");
        System.out.println(JSONObject.toJSONString(list));
    }
}
