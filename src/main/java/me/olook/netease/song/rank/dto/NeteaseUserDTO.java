package me.olook.netease.song.rank.dto;

/**
 * @author zhaohw
 * @date 2018-03-05 22:06
 */
public class NeteaseUserDTO {

    private String userId;

    private String avatar;

    private String nickName;

    public NeteaseUserDTO(String userId, String avatar, String nickName) {
        this.userId = userId;
        this.avatar = avatar;
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
