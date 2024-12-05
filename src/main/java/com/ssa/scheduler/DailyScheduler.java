package com.ssa.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyScheduler {

    @Scheduled(cron = "0 0/1 * * * *")
    private void scheduleDailyAdd() {
        System.out.println("Schedule");
    }

}
