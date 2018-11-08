package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.AppNotice;
import me.olook.netease.song.rank.repository.AppNoticeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-08 16:20
 */
@Service
public class AppNoticeBiz{

    @Resource
    private AppNoticeRepository repository;

    public List<AppNotice> findByType(String type){
        return repository.findByType(type);
    }
}
