package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.TemplateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-19 10:10
 */
@Repository
public interface TemplateMessageRepository extends JpaRepository<TemplateMessage,Integer> {

    /**
     * 查找过期模板
     * @param isValid 可用标志
     * @param date 过期时间点
     * @return 模板list
     */
    List<TemplateMessage> findByIsValidAndCrtTimeBefore(Integer isValid, Date date);

    /**
     * 查出某网易云用户被订阅所有可用模板
     * @param targetUserId 网易云id
     * @param isValid 可用标志
     * @return 模板list
     */
    List<TemplateMessage> findByTargetUserIdAndIsValid(String targetUserId,Integer isValid);

    /**
     * 查出某微信用户订阅的所有网易云用户可用模板
     * @param openId 微信openid
     * @param isValid 可用标志
     * @return 模板list
     */
    List<TemplateMessage> findByOpenidAndIsValid(String openId,Integer isValid);
}
