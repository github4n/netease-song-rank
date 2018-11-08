package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.AccessTokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhaohw
 * @date 2018-11-08 17:01
 */
@Repository
public interface AccessTokenInfoRepository extends JpaRepository<AccessTokenInfo,Integer> {

    AccessTokenInfo findFirstByIsValidOrderByCrtTimeDesc(Integer isvalid);
}
