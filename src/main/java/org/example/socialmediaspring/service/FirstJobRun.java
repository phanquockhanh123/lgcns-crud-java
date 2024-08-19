package org.example.socialmediaspring.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.socialmediaspring.common.CommonUtils;
import org.example.socialmediaspring.entity.TriggerInfo;
import org.example.socialmediaspring.jobs.FirstJob;
import org.example.socialmediaspring.scheduler.MainScheduler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirstJobRun {
    private  final MainScheduler scheduler;

    private final CommonUtils commonUtils;

    @PostConstruct
    public void init() {
        TriggerInfo triggerInfo = commonUtils.getTriggerInfoOfObj(5, false, 1000L, 1000L, "info");
        scheduler.scheduleJob(FirstJob.class, triggerInfo);
    }
}
