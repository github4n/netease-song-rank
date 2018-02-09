package me.olook.netease.song.rank.task;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import me.olook.netease.song.rank.annotation.TimerJobTypeName;
import me.olook.netease.song.rank.biz.SongRankDataBiz;
import me.olook.netease.song.rank.biz.SongRankDataDiffBiz;
import me.olook.netease.song.rank.biz.TimerJobBiz;
import me.olook.netease.song.rank.biz.TimerJobRecordBiz;
import me.olook.netease.song.rank.entity.SongRankData;
import me.olook.netease.song.rank.entity.SongRankDataDiff;
import me.olook.netease.song.rank.entity.TimerJob;
import me.olook.netease.song.rank.entity.TimerJobRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

/**
 * @author zhaohw
 * @date 2018-02-08 15:08
 */
@TimerJobTypeName(value = "听歌排行爬取任务")
public class SongRankTask implements Job {

    private Logger log = LoggerFactory.getLogger(SongRankTask.class);

    @Autowired
    private TimerJobBiz timerJobBiz ;

    @Autowired
    private SongRankDataBiz songRankDataBiz;

    @Autowired
    private SongRankDataDiffBiz songRankDataDiffBiz;

    @Autowired
    private TimerJobRecordBiz timerJobRecordBiz;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TimerJobRecord timerJobRecord = new TimerJobRecord();
        timerJobRecord.setStartTime(new Date());
        JobKey jobKey = context.getTrigger().getJobKey();
        TimerJob timerJob = new TimerJob();
        timerJob.setJobName(jobKey.getName());
        timerJob.setJobGroup(jobKey.getGroup());
        log.info("job name :" + jobKey.getName() + " job group :" +jobKey.getGroup() + " execute");
        TimerJob currentJob = timerJobBiz.selectOne(timerJob);

        try{
            WebClient wc = new WebClient(BrowserVersion.CHROME);
            wc.getOptions().setUseInsecureSSL(true);
            wc.getOptions().setJavaScriptEnabled(true);
            wc.getOptions().setRedirectEnabled(true);
            wc.setAjaxController(new NicelyResynchronizingAjaxController());
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setThrowExceptionOnScriptError(false);
            wc.getOptions().setTimeout(100000);
            wc.getOptions().setDoNotTrackEnabled(false);
            HtmlPage page = wc
                    .getPage("http://music.163.com/#/user/songs/rank?id="+currentJob.getTargetUserid());
            HtmlPage framePage = (HtmlPage)page.getFrameByName("contentFrame").getEnclosedPage();
            //累计听歌
            DomNodeList<DomElement> count = framePage.getElementsByTagName("h4");
            String countText = count.get(0).asText();
            List<SongRankData> songRankDataList = new ArrayList<SongRankData>(100);
            Document dom = Jsoup.parse(framePage.asXml());
            Elements lis = dom.select("li");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            StringBuilder sb = new StringBuilder();
            for(Element li : lis){
                if(!StringUtils.isEmpty(li.select("span.num").text())){
                    songRankDataList.add(
                            new SongRankData(
                                    uuid,
                                    Integer.parseInt(li.select(".num").text().replace(".","")),
                                    li.select("b").text(),
                                    li.select("span.s-fc8 > span").attr("title"),
                                    li.select("span.bg").attr("style").split(":")[1].replace(";","")
                            ));
                    sb.append(li.select(".num").text().replace(".",""));
                    sb.append(li.select("b").text());
                }

            }
            if(songRankDataList.size()==0){
                log.error(currentJob.getTargetUserid()+" 获取数据异常");
                return;
            }
            String snapshot = sb.toString().hashCode()+"";
            wc.close();
            TimerJobRecord oldRecord = timerJobRecordBiz.getLatestRecord(currentJob.getId());
            timerJobRecord.setNewData(0);
            if(oldRecord==null||!snapshot.equals(oldRecord.getSnapshot())){
                timerJobRecord.setNewData(1);
                //旧记录不为空 非第一次记录数据 需记录变化
                if (oldRecord != null) {
                    recordDiffData(getOldDataList(oldRecord.getId()),songRankDataList,uuid,currentJob.getTargetNickname());
                    log.info(currentJob.getJobName()+" 数据变更");
                }else{
                    log.info(currentJob.getJobName()+" 初始数据");
                }
                songRankDataBiz.insertByBatch(songRankDataList);
            }
            timerJobRecord.setSnapshot(snapshot);
            timerJobRecord.setId(uuid);
            timerJobRecord.setCount(Integer.parseInt(countText.substring(4,countText.indexOf("首"))));
            timerJobRecord.setJobId(currentJob.getId());
            timerJobRecord.setEndTime(new Date());
            timerJobRecordBiz.insert(timerJobRecord);
            log.info(currentJob.getJobName()+" 执行结束");
        }catch (IOException e){
            log.error(e.getMessage());
        }

    }


    private List<SongRankData> getOldDataList(String oldJobRecordId){
        Example example = new Example(SongRankData.class);
        example.createCriteria().andEqualTo("jobRecordId",oldJobRecordId);
        log.debug("上次执行记录id:"+oldJobRecordId);
        List<SongRankData> oldDataList = songRankDataBiz.selectByExample(example);
        log.debug("旧排行数据:"+oldDataList);
        return oldDataList;
    }

    private  void recordDiffData(List<SongRankData> oldDataList, List<SongRankData> newDataList,String jobRecordId ,String targetNickname){
        Map<String,SongRankData> oldMap = new HashMap<String, SongRankData>(oldDataList.size());
        Map<String,SongRankData> newMap = new HashMap<String, SongRankData>(newDataList.size());
        for(SongRankData songRankData : oldDataList){
            oldMap.put(songRankData.getSong(),songRankData);
        }
        for(SongRankData songRankData : newDataList){
            newMap.put(songRankData.getSong(),songRankData);
        }
        Iterator<Map.Entry<String, SongRankData>> entries = newMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, SongRankData> entry = entries.next();
            //新增歌曲
            if(oldMap.get(entry.getKey())==null){
                SongRankDataDiff songRankDataDiff = new SongRankDataDiff();
                songRankDataDiff.setJobRecordId(jobRecordId);
                songRankDataDiff.setTargetNickname(targetNickname);
                songRankDataDiff.setRankChange(-1);
                songRankDataDiff.setSong(entry.getKey());
                songRankDataDiff.setSinger(entry.getValue().getSinger());
                songRankDataDiff.setChangeTime(new Date());
                songRankDataDiffBiz.insert(songRankDataDiff);
            }
            //旧榜存在 排行不同 且新榜大于旧榜排序
            else{
                Integer newRank = entry.getValue().getRank();
                Integer oldRank = oldMap.get(entry.getKey()).getRank();
                if(newRank<oldRank){
                    SongRankDataDiff songRankDataDiff = new SongRankDataDiff();
                    songRankDataDiff.setJobRecordId(jobRecordId);
                    songRankDataDiff.setTargetNickname(targetNickname);
                    songRankDataDiff.setRankChange(oldRank-newRank);
                    songRankDataDiff.setSong(entry.getKey());
                    songRankDataDiff.setSinger(entry.getValue().getSinger());
                    songRankDataDiff.setChangeTime(new Date());
                    songRankDataDiffBiz.insert(songRankDataDiff);
                }
            }
        }
    }
}
