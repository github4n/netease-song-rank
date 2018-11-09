package me.olook.netease.song.rank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhaohw
 * @date 2018-11-08 15:01
 */
@EnableScheduling
@SpringBootApplication
public class NeteaseSongRankBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(NeteaseSongRankBootStrap.class, args);
    }
}
