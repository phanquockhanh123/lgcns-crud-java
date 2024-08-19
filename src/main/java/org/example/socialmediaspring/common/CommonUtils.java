package org.example.socialmediaspring.common;

import org.example.socialmediaspring.entity.TriggerInfo;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommonUtils {
    public JobDetail getJobDetail(Class className, TriggerInfo info){
        JobDataMap jobData = new JobDataMap();
        jobData.put(className.getSimpleName(),info);
        return JobBuilder.newJob(className)
                .withIdentity(className.getSimpleName(),"grp1")
                .storeDurably(false)
                .requestRecovery(false)
                .setJobData(jobData)
                .build();
    }
    public JobDetail getJobDetail(Class className){
        return JobBuilder.newJob(className)
                .withIdentity(className.getSimpleName(),"grp1")
                .storeDurably(false)
                .build();
    }

    public Trigger getTriggerInfoOfJob(Class className, TriggerInfo info) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMilliseconds(info.getTimeInterval());

        if (info.isRunForever()) {
            builder.repeatForever();
        } else  {
            builder.withRepeatCount(info.getTriggerCount());

        }

        return TriggerBuilder.newTrigger()
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffSet()))
                .withSchedule(builder)
                .build();
    }

    public TriggerInfo getTriggerInfoOfObj(int triggerCount, boolean runForever, Long repeatValue, Long initialOfSet, String information) {
        TriggerInfo info = new TriggerInfo();
        info.setRunForever(runForever);
        info.setTriggerCount(triggerCount);
        info.setInitialOffSet(initialOfSet);
        info.setTimeInterval(repeatValue);
        info.setInfo(information);

        return info;

    }

    public Trigger getTriggerByCronExpression(Class className, String expression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(className.getSimpleName())
                .withSchedule(CronScheduleBuilder.cronSchedule(expression))
                .build();
    }

}
