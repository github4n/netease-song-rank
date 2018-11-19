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
     * @param isValid 可以标志
     * @param date 过期时间点
     * @return 模板list
     */
    List<TemplateMessage> findByIsValidAndCrtTimeBefore(Integer isValid, Date date);
}
