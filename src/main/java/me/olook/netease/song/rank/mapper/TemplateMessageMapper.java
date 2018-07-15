package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.TemplateMessage;
import tk.mybatis.mapper.common.Mapper;

/** 
* 模板消息
* @author zhaohw
* @date 2018/3/8 13:08
*/
public interface TemplateMessageMapper extends Mapper<TemplateMessage>{

    /**
     * 更新token过期的模板消息记录
     */
    int updateExpired();
}
