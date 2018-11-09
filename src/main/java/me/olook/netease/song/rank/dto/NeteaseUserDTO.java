package me.olook.netease.song.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaohw
 * @date 2018-03-05 22:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeteaseUserDTO {

    private String userId;

    private String avatar;

    private String nickName;
}
