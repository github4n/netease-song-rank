package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.AppNoticeBiz;
import me.olook.netease.song.rank.entity.AppNotice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-07-15 9:56
 */
@Controller
@RequestMapping("notice")
@Api(description = "系统公告模块")
public class AppNoticeController extends BaseController<AppNoticeBiz,AppNotice> {

    @ApiOperation(value = "通过类型查询")
    @RequestMapping(value = "/type/{type}",method = RequestMethod.GET)
    @ResponseBody
    public List<AppNotice> findByType(@PathVariable String type){
        return baseBiz.findByType(type);
    }
}
