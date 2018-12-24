package me.olook.netease.song.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaohw
 * @date 2018-12-24 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeNetEaseUserDTO{

    private String userId;

    private String avatar;

    private String nickName;
    /**
     * 可用模板消息数量
     */
    private Long subscribe;
}
