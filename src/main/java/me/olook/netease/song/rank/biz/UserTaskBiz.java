package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.constants.TimerJobUpdateNameEnum;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.entity.WxUser;
import me.olook.netease.song.rank.exception.UserTaskException;
import me.olook.netease.song.rank.repository.TimerJobRepository;
import me.olook.netease.song.rank.repository.UserRefJobRepository;
import me.olook.netease.song.rank.repository.WxUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zhaohw
 * @date 2018-11-16 14:30
 */
@Service
public class UserTaskBiz {

    /**
     * 每个用户默认可创建任务数量
     */
    private final static Integer DEFAULT_JOB_NUM = 3;

    @Resource
    private UserRefJobRepository userRefJobRepository;

    @Resource
    private WxUserRepository wxUserRepository;

    @Resource
    private TimerJobRepository timerJobRepository;

    @Resource
    private BaseQuartzBiz baseQuartzBiz;

    @Transactional(rollbackFor = Exception.class)
    public int addTask(UserRefJob userRefJob) throws UserTaskException {
        List<UserRefJob> jobs = userRefJobRepository.findByOpenIdAndDelFlag(userRefJob.getOpenId(), 0);
        WxUser user = wxUserRepository.findByOpenId(userRefJob.getOpenId());
        int jobLimit = user == null ? DEFAULT_JOB_NUM : user.getJobLimit();
        if(jobs.size()>=jobLimit){
            throw new UserTaskException("您最多只能关注"+jobLimit+"名好友!");
        }
        boolean isFollowed = jobs.stream().allMatch(p->userRefJob.getTargetUserId().equals(p.getTargetUserId()));
        if(isFollowed){
            throw new UserTaskException("您已经关注过Ta了~");
        }
        //查找是否爬虫任务已经存在
        TimerJob timerJob = timerJobRepository.findByTargetUserId(userRefJob.getTargetUserId());

        if(timerJob == null){
            TimerJob newJob = userRefJobToJob(userRefJob);
            timerJobRepository.save(newJob);
            baseQuartzBiz.createTimerJob(newJob);
        }else if(TimerJob.STATUS_EXPIRED == timerJob.getStatus()||
                TimerJob.STATUS_STOP == timerJob.getStatus()){
            timerJob.setStatus(TimerJob.STATUS_RUN);
            timerJob.setUpdTime(new Date());
            timerJob.setUpdName(TimerJobUpdateNameEnum.RERUN);
            timerJobRepository.save(timerJob);
            baseQuartzBiz.createTimerJob(timerJob);
        }
        userRefJob.setCrtTime(new Date());
        userRefJobRepository.save(userRefJob);
        List<UserRefJob> followList = userRefJobRepository.findByTargetUserIdAndDelFlag(userRefJob.getTargetUserId(), 0);
        return followList.size();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeTask(int userRefJobId){
        Optional<UserRefJob> userRefJobOptional = userRefJobRepository.findById(userRefJobId);
        if(userRefJobOptional.isPresent()){
            UserRefJob userRefJob = userRefJobOptional.get();
            List<UserRefJob> userRefJobs =
                    userRefJobRepository.findByTargetUserIdAndDelFlag(userRefJob.getTargetUserId(), 0);
            if(userRefJobs.size() == 1){
                TimerJob timerJob = timerJobRepository.findByTargetUserId(userRefJob.getTargetUserId());
                timerJob.setStatus(TimerJob.STATUS_STOP);
                timerJobRepository.save(timerJob);
                baseQuartzBiz.deleteScheduleJob(timerJob.getJobName(),timerJob.getJobGroup());
            }
            userRefJob.setDelFlag(1);
            userRefJob.setUpdTime(new Date());
            userRefJobRepository.save(userRefJob);
            return true;
        }
        return false;
    }

    private TimerJob userRefJobToJob(UserRefJob userRefJob){
        TimerJob newJob = new TimerJob();
        String expression = LocalDateTime.now().getSecond()+" */1 * * * ?";
        newJob.setCronExpression(expression);
        newJob.setJobName(userRefJob.getTargetUserId());
        newJob.setJobGroup("group1");
        newJob.setStatus(1);
        newJob.setJobType("听歌排行爬取任务");
        newJob.setTargetUserId(userRefJob.getTargetUserId());
        newJob.setTargetNickname(userRefJob.getTargetNickname());
        newJob.setCrtUser(userRefJob.getOpenId());
        newJob.setCrtTime(new Date());
        newJob.setUpdTime(new Date());
        newJob.setUpdName("new job");
        return newJob;
    }
}
