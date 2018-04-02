package me.olook.netease.song.rank;

import me.olook.netease.song.rank.util.ProxyUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-05-25 12:44
 */
@EnableTransactionManagement
@SpringBootApplication
@ServletComponentScan("me.olook.netease.song.rank.config.druid")
public class TimerServerBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TimerServerBootstrap.class).web(true).run(args);
        ProxyUtil.init();
        }
}
