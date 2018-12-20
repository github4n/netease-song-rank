package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.entity.TemplateMessage;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.repository.TemplateMessageRepository;
import me.olook.netease.song.rank.repository.UserRefJobRepository;
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

    private final static String TEMPLATE_ID = "AIL1AXTIKfmmifc4uPpCthIiNi-AMgMSxXBvXihnPOg";

    @Resource
    private UserRefJobRepository userRefJobRepository;

    @Resource
    private TemplateMessageRepository templateMessageRepository;

    /**
     * 清除过期模板
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateExpiredTemplates(){
        Instant instant = Instant.now().minus(DAY_DIFF, ChronoUnit.DAYS);
        List<TemplateMessage> expiredTemplates =
                templateMessageRepository.findByIsValidAndCrtTimeBefore(TemplateMessage.VALID, Date.from(instant));
        expiredTemplates.forEach(templateMessage -> {
            templateMessage.setIsValid(TemplateMessage.EXPIRED);
            templateMessage.setUpdTime(new Date());
            templateMessageRepository.save(templateMessage);
        });
        return expiredTemplates.size();
    }

    /**
     * 找出某用户所有可用模板 用于推送
     */
    public List<TemplateMessage> findValidTemplates(String targetUserId){
        return templateMessageRepository.findByTargetUserIdAndIsValid(targetUserId,TemplateMessage.VALID);
    }

    public TemplateMessage addTemplates(TemplateMessage templateMessage){
        //是否存在该微信用户对该网易云用户的可用推送模板
        List<TemplateMessage> templates =
                templateMessageRepository.findByOpenidAndIsValid(templateMessage.getOpenid(), TemplateMessage.VALID);
        final boolean exist = templates.stream().anyMatch(t -> {
            return t.getTargetUserId().equals(templateMessage.getTargetUserId());
        });
        if(exist) return null;

        templateMessage.setCrtTime(new Date());
        List<UserRefJob> userRefJobs =
                userRefJobRepository.findByTargetUserIdAndDelFlag(templateMessage.getTargetUserId(),0);
        String userName = userRefJobs.size()==0?"":userRefJobs.get(0).getTargetNickname();

        templateMessage.setPage("pages/record/record?userId="+templateMessage.getTargetUserId()
                +"&tusername="+userName);
        templateMessage.setTemplateId(TEMPLATE_ID);
        templateMessage.setIsValid(TemplateMessage.VALID);
        return templateMessageRepository.save(templateMessage);
    }

    public TemplateMessage save(TemplateMessage templateMessage){
        return templateMessageRepository.save(templateMessage);
    }

}
