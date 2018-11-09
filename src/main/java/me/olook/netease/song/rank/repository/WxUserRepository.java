package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhaohw
 * @date 2018-11-09 10:39
 */
@Repository
public interface WxUserRepository extends JpaRepository<WxUser,Integer> {

    WxUser findByOpenId(String openid);
}
