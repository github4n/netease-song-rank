package me.olook.netease.song.rank.util.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zhaohw
 * @date 2018-04-03 10:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyInfo {

    private String ip;

    private Integer port;

    private LocalDateTime createTime;

    public ProxyInfo(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString(){
        return ip+":"+port;
    }
}
