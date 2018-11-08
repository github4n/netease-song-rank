package me.olook.netease.song.rank.mapper;

import me.olook.netease.song.rank.entity.AppNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-08 15:31
 */
@Repository
public interface AppNoticeRepository extends JpaRepository<AppNotice,Integer> {

    List<AppNotice> findByType(String type);
}
