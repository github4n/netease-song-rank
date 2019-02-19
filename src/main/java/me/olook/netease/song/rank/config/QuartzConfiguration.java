//package me.olook.netease.song.rank.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.PropertiesFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * @author zhaohw
// * @date 2019-01-24 11:39
// */
//@Configuration
//public class QuartzConfiguration {
//
//    private final CustomJobFactory customJobFactory;
//
//    @Autowired
//    public QuartzConfiguration(CustomJobFactory customJobFactory) {
//        this.customJobFactory = customJobFactory;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setOverwriteExistingJobs(true);
//        factory.setStartupDelay(10);
//        factory.setQuartzProperties(quartzProperties());
//        factory.setJobFactory(customJobFactory);
//        return factory;
//    }
//
//    /**
//     * 加载quartz数据源配置
//     */
//    @Bean
//    public Properties quartzProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.yml"));
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }
//}
