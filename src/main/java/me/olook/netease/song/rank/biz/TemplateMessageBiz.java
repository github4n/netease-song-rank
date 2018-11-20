package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.TemplateMessage;
import me.olook.netease.song.rank.repository.TemplateMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @author zhaohw
 * @date 2018-11-19 10:14
 */
@Service
public class TemplateMessageBiz {

    private final static int DAY_DIFF = 7;

    @Resource
    private TemplateMessageRepository templateMessageRepository;

    /**
     * 清除过期模板
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateExpiredTemplates(){
        Instant instant = Instant.now();
        instant.minus(DAY_DIFF, ChronoUnit.DAYS);
        List<TemplateMessage> expiredTemplates =
                templateMessageRepository.findByIsValidAndCrtTimeBefore(TemplateMessage.VALID, Date.from(instant));
        expiredTemplates.forEach(templateMessage -> {
            templateMessage.setIsValid(TemplateMessage.EXPIRED);
            templateMessage.setUpdTime(new Date());
            templateMessageRepository.save(templateMessage);
        });
        return expiredTemplates.size();
    }

    public List<TemplateMessage> findValidTemplates(String targetUserId){
        return templateMessageRepository.findByTargetUserIdAndIsValid(targetUserId,TemplateMessage.VALID);
    }
}
