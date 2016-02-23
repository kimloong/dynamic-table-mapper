package com.closer.common.config;

import com.closer.employee.job.HappyBirthdayJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by closer on 2016/2/22.
 */
@Configuration
public class QuartzConfig {

    public static final String EMPLOYEE_HAPPY_BIRTHDAY = "Employee-HappyBirthdayJob";
    public static final String JOB = "Job";

    @Bean
    public SchedulerFactoryBean quartzScheduler(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setApplicationContext(applicationContext);
        scheduler.setConfigLocation(new ClassPathResource("quartz.properties"));
        scheduler.afterPropertiesSet();
        return scheduler;
    }

    @Bean
    public JobDetail happyBirthday(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = newJob(HappyBirthdayJob.class)
                .withIdentity(JOB, EMPLOYEE_HAPPY_BIRTHDAY)
                .requestRecovery()
                .storeDurably()
                .build();
        scheduler.addJob(jobDetail, true);
        return jobDetail;
    }
}
