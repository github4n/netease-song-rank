package me.olook.netease.song.rank.repository;

import me.olook.netease.song.rank.entity.UserRefJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-09 10:43
 */
@Repository
public interface UserRefJobRepository extends JpaRepository<UserRefJob,Integer> {

    List<UserRefJob> findByTargetUserIdAndDelFlag(String targetUserId, Integer delFlag);

    List<UserRefJob> findByOpenIdAndDelFlag(String openid,Integer delFlag);
}
