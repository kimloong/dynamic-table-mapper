package com.closer.common.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * Created by closer on 2016/2/22.
 */
@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean quartzScheduler(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setApplicationContext(applicationContext);
        scheduler.setConfigLocation(new ClassPathResource("quartz.properties"));
        scheduler.afterPropertiesSet();
        return scheduler;
    }
}
