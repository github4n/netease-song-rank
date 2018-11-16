package me.olook.netease.song.rank.util.wechat;

/**
 * @author zhaohw
 * @date 2018-11-16 11:36
 */
public interface WeChatApiUrl {

    String JS_CODE_TO_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

    String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

    String SEND_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";
}
