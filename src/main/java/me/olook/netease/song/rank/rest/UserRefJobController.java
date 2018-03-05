package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.UserRefJobBiz;
import me.olook.netease.song.rank.entity.UserRefJob;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaohw
 * @date 2018-03-05 20:47
 */
@Controller
@RequestMapping("userjob")
@Api(description = "用户任务模块")
public class UserRefJobController extends BaseController<UserRefJobBiz,UserRefJob> {


}
