package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.biz.AccessTokenInfoBiz;
import me.olook.netease.song.rank.biz.AppNoticeBiz;
import me.olook.netease.song.rank.entity.AppNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-08 15:28
 */
@RestController
@RequestMapping("/notice")
@Api(description = "系统公告模块")
public class AppNoticeController {

    @Autowired
    private AccessTokenInfoBiz tokenInfoBiz;

    @Resource
    private AppNoticeBiz appNoticeBiz;

    @ApiOperation(value = "通过类型查询")
    @GetMapping(value = "/type/{type}")
    public List<AppNotice> findByType(@PathVariable String type){
        System.out.println(tokenInfoBiz.getLatestValidToken());
        return appNoticeBiz.findByType(type);
    }
}
