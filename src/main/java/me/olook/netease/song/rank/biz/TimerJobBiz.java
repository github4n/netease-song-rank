package me.olook.netease.song.rank.biz;

import me.olook.netease.song.rank.constants.TimerJobUpdateNameEnum;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.UserRefJob;
import me.olook.netease.song.rank.repository.TimerJobRepository;
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
 * @date 2018-11-09 9:49
 */
@Service
public class TimerJobBiz {

    private final static int DAY_DIFF = 8;

    @Resource
    private TimerJobRepository timerJobRepository;

    @Resource
    private UserRefJobRepository userRefJobRepository;

    @Resource
    private BaseQuartzBiz baseQuartzBiz;

    public TimerJob findByTargetUserId(String userId){
        return timerJobRepository.findByTargetUserId(userId);
    }

    public TimerJob save(TimerJob timerJob){
        return timerJobRepository.save(timerJob);
    }

    public List<TimerJob> findAll(){
        return timerJobRepository.findAll();
    }

    public List<TimerJob> findExpiredTimerJob(){
        Instant instant = Instant.now();
        instant.minus(DAY_DIFF, ChronoUnit.DAYS);
        return timerJobRepository.findTimerJobsByStatusAndUpdTimeBefore(TimerJob.STATUS_RUN, Date.from(instant));
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateExpiredTimerJobs(){
        List<TimerJob> expiredTimerJobs = this.findExpiredTimerJob();
        expiredTimerJobs.forEach(p->{
            baseQuartzBiz.deleteScheduleJob(p.getJobName(),p.getJobGroup());
            p.setStatus(TimerJob.STATUS_EXPIRED);
            p.setUpdTime(new Date());
            p.setUpdName(TimerJobUpdateNameEnum.CLEAN);
            this.save(p);
            List<UserRefJob> userJobs = userRefJobRepository.findByTargetUserIdAndDelFlag(p.getTargetUserId(), 0);
            userJobs.forEach(job->{
                job.setDelFlag(1);
                userRefJobRepository.save(job);
            });

        });
        return expiredTimerJobs.size();
    }
}
