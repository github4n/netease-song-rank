package me.olook.netease.song.rank.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.olook.netease.song.rank.constants.RankRecordResponseCode;
import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.exception.DataResolveException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 数据解析器
 * @author zhaohw
 * @date 2018-11-20 10:21
 */
@Slf4j
public class RecordRankResolver {

    public static List<SongRankData> parseData(JSONObject data) throws DataResolveException {
        List<SongRankData> songRankDataList;
        Integer code = data.getInteger("code");
        if(!code.equals(RankRecordResponseCode.SUCCESS)){
            throw new DataResolveException(code);
        }
        JSONArray array = data.getJSONArray("weekData");
        songRankDataList = IntStream.range(0, array.size())
                .mapToObj(i -> parseObject(i, array.get(i)))
                .collect(Collectors.toCollection(() -> new ArrayList<>(100)));
        return songRankDataList;
    }

    private static SongRankData parseObject(Integer index, Object weekDataItem){
        JSONObject item = JSON.parseObject(weekDataItem.toString());
        String ratio = item.getString("score");
        JSONObject song = item.getJSONObject("song");
        Integer songId = song.getInteger("id");
        String songName = song.getString("name");
        String picUrl = song.getJSONObject("al").getString("picUrl");
        JSONArray ar = song.getJSONArray("ar");
        // todo 多singer处理
        String singer = ar.getJSONObject(0).getString("name");
        return SongRankData.builder()
                .rank(index+1)
                .ratio(ratio)
                .songId(songId)
                .song(songName)
                .singer(singer)
                .picUrl(picUrl)
                .build();
    }
}
