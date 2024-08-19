package org.example.socialmediaspring.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.example.socialmediaspring.common.CommonUtils;
import org.example.socialmediaspring.entity.TriggerInfo;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MainScheduler {
    private  final Scheduler scheduler;
    private final CommonUtils commonUtils;

    @PostConstruct
    public void startSchedule() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    // job details, trigger info
    public  void scheduleJob(Class className, TriggerInfo info) {

        try {
            JobDetail jobDetail = commonUtils.getJobDetail(className, info);

            Trigger triggerDetail = commonUtils.getTriggerInfoOfJob(className, info);

            scheduler.scheduleJob(jobDetail, triggerDetail);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    // job details, trigger info
    public  void scheduleJob(Class className, String cronExp) {

        try {
            JobDetail jobDetail = commonUtils.getJobDetail(className);

            Trigger triggerDetail = commonUtils.getTriggerByCronExpression(className, cronExp);

            scheduler.scheduleJob(jobDetail, triggerDetail);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void closeScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
