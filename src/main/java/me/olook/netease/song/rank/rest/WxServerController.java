package me.olook.netease.song.rank.rest;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.config.WxAppProperties;
import me.olook.netease.song.rank.dto.NeteaseUserDTO;
import me.olook.netease.song.rank.dto.SongRankDiffListDTO;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.util.netease.NeteaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    private Logger log = LoggerFactory.getLogger(WxServerController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxAppProperties wxApp;

    @Autowired
    private UserRefJobBiz userRefJobBiz;

    @Autowired
    private TimerJobBiz timerJobBiz;

    @Autowired
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @ApiOperation(value = "获取用户关联的任务")
    @RequestMapping(value = "getUerJob", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getUerJob(String openid) {
        List<UserRefJob> jobList = userRefJobBiz.getUserJobByOpenId(openid);
        String json = JSONObject.toJSONString(jobList);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "获取用户openid")
    @RequestMapping(value = "jsCode2Session", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getWxUserInfo(String code) {
//        JSONObject jsonObject = JSON.parseObject(jsCodeToSession(code));
//        String openid = jsonObject.get("openid").toString();
//        String sessionKey = jsonObject.get("session_key").toString();
//        String unionid = jsonObject.get("unionid").toString();
        String json = jsCodeToSession(code);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "获取网易云用户")
    @RequestMapping(value = "getNeteaseUser", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getNeteaseUser(String keyWord) {
        List<NeteaseUserDTO> list = NeteaseUtil.searchUser(keyWord);
        String json = JSONObject.toJSONString(list);
        log.debug(json);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "测试能否获取排行数据")
    @RequestMapping(value = "checkSongRank", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> checkSongRank(String userId) {
        JSONObject result = NeteaseUtil.songRank(userId);
        if(result!=null){
            String code = result.get("code").toString();
            if("200".equals(code)){
                return ResponseEntity.status(200).body("true");
            }
        }
        return ResponseEntity.status(403).body("false");
    }

    @ApiOperation(value = "获取排行变化数据")
    @RequestMapping(value = "getSameTypeRecord", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getSameTypeRecord(String userId) {
        SongRankDiffListDTO songRankDiffDTO = new  SongRankDiffListDTO();
        //查出最近10条记录
        List<SongRankDataDiff> dataDiffs = songRankDataDiffBiz.getLatestRecordLimit(userId,10);
        List<SongRankDataDiff> responseList = new ArrayList<>();
        for(SongRankDataDiff diff : dataDiffs){
            if(diff.getCount()==1){
                responseList.add(diff);
            }
        }
        if(dataDiffs.size()==0){
            return ResponseEntity.status(404).body(JSONObject.toJSONString(songRankDiffDTO));
        }
        //没有有效数据
        if(responseList.size()==0){
            songRankDiffDTO.setIsBatchUpdate(1);
            songRankDiffDTO.setList(dataDiffs);
        }else{
            songRankDiffDTO.setIsBatchUpdate(0);
            songRankDiffDTO.setList(responseList);
        }
        String json = JSONObject.toJSONString(songRankDiffDTO);
        return ResponseEntity.status(200).body(json);
    }

    @ApiOperation(value = "获取排行变化数据")
    @RequestMapping(value = "getRecord", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getSongRankRecord(String userId) {
        SongRankDiffListDTO songRankDiffDTO = new  SongRankDiffListDTO();
        //查看任务是否已经停止
        TimerJob timerJob = timerJobBiz.findByTargetUserId(userId);
        if(timerJob == null||TimerJob.STATUS_STOP.equals(timerJob.getStatus())){
            return ResponseEntity.status(403).body("Ta好像已经隐藏了自己的听歌记录,建议尝试取消再重新关注Ta.");
        }
        //查出最近10条记录
        List<SongRankDataDiff> dataDiffs = songRankDataDiffBiz.getLatestRecordLimit(userId,10);
        songRankDiffDTO.setIsBatchUpdate(-1);
        songRankDiffDTO.setList(dataDiffs);
        String json = JSONObject.toJSONString(songRankDiffDTO);
        return ResponseEntity.status(200).body(json);
    }


    private  String jsCodeToSession(String code){
        String params = "?appid=" + wxApp.getAppId() + "&secret=" + wxApp.getAppSecret()
                + "&js_code=" + code + "&grant_type=" + wxApp.getGrantType();
        String url = "https://api.weixin.qq.com/sns/jscode2session" + params;
        String wxJson = restTemplate.getForEntity(url, String.class).getBody();
        log.debug("code :"+code+" to session :"+wxJson);
        return wxJson;
    }
}