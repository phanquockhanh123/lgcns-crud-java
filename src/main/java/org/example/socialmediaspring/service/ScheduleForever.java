package org.example.socialmediaspring.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.socialmediaspring.common.CommonUtils;
import org.example.socialmediaspring.entity.TriggerInfo;
import org.example.socialmediaspring.jobs.SecondJob;
import org.example.socialmediaspring.scheduler.MainScheduler;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleForever {
    private final MainScheduler scheduler;

    private  final CommonUtils commonUtils;

    @PostConstruct
    public void init() {

        // TriggerInfo info = commonUtils.getTriggerInfoOfObj(1, true, 1000L, 1000L, "hello");

        scheduler.scheduleJob(SecondJob.class, "0/2 * * * * ? ");

    }


}
