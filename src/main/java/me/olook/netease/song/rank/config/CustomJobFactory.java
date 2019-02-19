//package me.olook.netease.song.rank.config;
//
//import org.quartz.spi.TriggerFiredBundle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
//import org.springframework.scheduling.quartz.AdaptableJobFactory;
//import org.springframework.stereotype.Component;
//
///**
// * @author zhaohw
// * @date 2019-01-24 11:41
// */
//@Component
//public class CustomJobFactory extends AdaptableJobFactory {
//
//    private final AutowireCapableBeanFactory capableBeanFactory;
//
//    @Autowired
//    public CustomJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
//        this.capableBeanFactory = capableBeanFactory;
//    }
//
//    @Override
//    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
//        Object jobInstance = super.createJobInstance(bundle);
//        capableBeanFactory.autowireBean(jobInstance);
//        return jobInstance;
//    }
//}
