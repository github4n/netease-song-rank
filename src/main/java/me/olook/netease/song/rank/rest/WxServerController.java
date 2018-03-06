package me.olook.netease.song.rank.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.config.WxAppProperties;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.util.NeteaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-03-05 13:54
 */
@Controller
@RequestMapping("wx")
@Api(description = "微信服务器请求模块")
@EnableConfigurationProperties(WxAppProperties.class)
public class WxServerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxAppProperties wxApp;

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @Autowired
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @ApiOperation(value = "获取用户关联的任务")
    @RequestMapping(value = "getUerJob", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getUerJob(String code) {
        String params = "?appid=" + wxApp.getAppId() + "&secret=" + wxApp.getAppSecret() + "&js_code=" + code + "&grant_type=" + wxApp.getGrantType();
        String url = "https://api.weixin.qq.com/sns/jscode2session" + params;
        String wxJson = restTemplate.getForEntity(url, String.class).getBody();
        JSONObject jsonObject = JSON.parseObject(wxJson);
        String openid = jsonObject.get("openid").toString();
        String sessionKey = jsonObject.get("session_key").toString();
        System.out.println(openid);
        List<UserRefJob> jobList = userRefJobBiz.getUserJobByOpenId(openid);
        String json = JSONObject.toJSONString(jobList);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "获取网易云用户")
    @RequestMapping(value = "getNeteaseUser", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getNeteaseUser(String keyWord) {
        List<NeteaseUserDTO> list = NeteaseUtil.searchUser(keyWord);
        String json = JSONObject.toJSONString(list);
        System.out.println(json);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "测试能否获取排行数据")
    @RequestMapping(value = "checkSongRank", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> checkSongRank(String userId) {
        String result = NeteaseUtil.songRank(userId);
        if(result!=null){
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.get("code").toString();
            if("200".equals(code)){
                return ResponseEntity.status(200).body("true");
            }
        }
        return ResponseEntity.status(403).body("false");
    }

    @ApiOperation(value = "获取排行变化数据")
    @RequestMapping(value = "getRecord", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getSongRankRecord(String userId) {
        Example example = new Example(SongRankDataDiff.class);
        example.createCriteria().andEqualTo("targetUserId",userId);
        List<SongRankDataDiff> dataDiffs = songRankDataDiffBiz.selectByExample(example);
        if(dataDiffs.size()>0){
            String json = JSONObject.toJSONString(dataDiffs);
            return ResponseEntity.status(200).body(json);
        }
        return ResponseEntity.status(404).body("no data");
    }
}