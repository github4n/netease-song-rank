package me.olook.netease.song.rank.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebServlet;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-20 21:34
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*")
public class DruidStatViewServlet extends StatViewServlet {


}
