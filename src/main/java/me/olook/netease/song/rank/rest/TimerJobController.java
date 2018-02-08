package me.olook.netease.song.rank.rest;

import io.swagger.annotations.Api;
import me.olook.netease.song.rank.base.BaseController;
import me.olook.netease.song.rank.biz.BaseQuartzBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.entity.TimerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaohw
 * @date 2018-01-10 17:46
 */
@Controller
@RequestMapping("timer")
@Api(description = "定时任务模块")
public class TimerJobController extends BaseController<TimerJobBiz,TimerJob>{

    @Autowired
    private BaseQuartzBiz baseQuartzBiz;
}
