package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.base.BaseBiz;
import me.olook.netease.song.rank.entity.AppNotice;
import me.olook.netease.song.rank.mapper.AppNoticeMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 系统公告
 * @author zhaohw
 * @date 2018-07-15 9:55
 */
@Service
public class AppNoticeBiz extends BaseBiz<AppNoticeMapper,AppNotice> {

    public List<AppNotice> findByType(String type){
        Example example = new Example(AppNotice.class);
        example.createCriteria().andEqualTo("type",type).andEqualTo("delFlag","0");
        return mapper.selectByExample(example);
    }
}
