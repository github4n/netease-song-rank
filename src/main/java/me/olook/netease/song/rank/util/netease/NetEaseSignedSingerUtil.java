package me.olook.netease.song.rank.util.netease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.olook.netease.song.rank.util.netease.NetEaseHttpClient.getSongRankData;
import static me.olook.netease.song.rank.util.netease.NetEaseHttpClient.post;

/**
 * @author zhaohw
 * @date 2018-12-17 16:46
 */
public class NetEaseSignedSingerUtil {


    private static Integer offset = 0;

    private static Integer counter = 0;

    private static List<NetEaseSignedSinger> signedSingerList = new ArrayList<>();

    /**
     * 获取签约歌手
     * {categoryCode: "5001", offset: "180", total: "false", limit: "60", csrf_token: ""}
     */
    public static void getSignedSinger(Integer start){
        Map<String,String> map = Maps.newHashMap();
        map.put("categoryCode","5001");
        map.put("offset",start.toString());
        map.put("total","false");
        map.put("limit","80");
        map.put("csrf_token","");
        String json = JSONObject.toJSONString(map);
        String params = NetEaseEncryptUtil.getUrlParams(json);
        String url = NetEaseApiUrl.SIGNED+params;
        String postResult = post(url,null,null);
        JSONObject jsonObject = JSONObject.parseObject(postResult);
        JSONArray artists = jsonObject.getJSONArray("artists");

        artists.forEach(p->{
            JSONObject jsonObject1 = JSONObject.parseObject(p.toString());
            JSONObject songRankData = getSongRankData(jsonObject1.getString("accountId"));
            if(songRankData!=null && songRankData.getInteger("code").equals(200)){
                int size = songRankData.getJSONArray("weekData").size();
                System.out.println(jsonObject1.getString("accountId")+" : " +jsonObject1.getString("name")+" : "+size);
                signedSingerList.add(new NetEaseSignedSinger(jsonObject1.getString("accountId"),jsonObject1.getString("name"),size));
                counter += artists.size();
            }

        });
        if(jsonObject.getBoolean("more")){
            offset++;
            getSignedSinger(offset*80);
        }else{
            signedSingerList.stream().sorted((x,y)->{
                return y.getRankNum().compareTo(x.getRankNum());
            }).forEach(e->{
                System.out.println(e.toSql());
            });
            System.out.println("共: "+(counter/80));
        }
    }

    public static void main(String[] args) {
        getSignedSinger(0);
    }
}
