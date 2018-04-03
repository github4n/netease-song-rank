package me.olook.netease.song.rank.util;

/**
 * @author zhaohw
 * @date 2018-04-03 10:43
 */
public class ProxyInfo {

    public ProxyInfo(){

    }

    public ProxyInfo(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
    private String ip;

    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ProxyInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
