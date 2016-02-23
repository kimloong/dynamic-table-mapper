package com.closer.employee.job;

import com.closer.common.config.QuartzConfig;
import com.closer.employee.domain.Employee;
import org.quartz.*;

import java.util.Calendar;
import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 发送生日福语任务
 * Created by closer on 2016/2/23.
 */
public class HappyBirthdayJob implements Job {

    public static final String NAME = "Name";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getTrigger().getJobDataMap();
        String name = (String) data.get(NAME);
        System.out.println("--------------------------------");
        System.out.println("Happy Birthday to " + name);
        System.out.println("--------------------------------");
    }

    public static void trigger(Scheduler scheduler, Employee employee) throws SchedulerException {
        long id = employee.getId();
        String triggerName = String.valueOf(id);
        scheduler.unscheduleJob(
                TriggerKey.triggerKey(triggerName,
                        QuartzConfig.EMPLOYEE_HAPPY_BIRTHDAY)
        );

        Date birthday = employee.getBirthday();
        if (birthday != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthday);
            String cronExpression = String.format("0 10 23 %d %d ?",
                    calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH)) + 1;
            Trigger trigger = newTrigger()
                    .forJob(QuartzConfig.JOB, QuartzConfig.EMPLOYEE_HAPPY_BIRTHDAY)
                    .withIdentity(triggerName, QuartzConfig.EMPLOYEE_HAPPY_BIRTHDAY)
                    .usingJobData(NAME, employee.getName())
                    .startNow()
                    .withSchedule(cronSchedule(cronExpression))
                    .build();
            scheduler.scheduleJob(trigger);
        }
    }
}
