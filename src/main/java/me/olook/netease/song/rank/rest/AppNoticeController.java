package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.AppNoticeBiz;
import me.olook.netease.song.rank.entity.AppNotice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaohw
 * @date 2018-07-15 9:56
 */
@Controller
@RequestMapping("notice")
@Api(description = "系统公告模块")
public class AppNoticeController extends BaseController<AppNoticeBiz,AppNotice> {

}
