package com.huybq.fund_management.domain.schedule;

import com.huybq.fund_management.domain.schedule.quartz.manager.QuartzScheduleManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleStartupRunner {
    private final QuartzScheduleManager quartzScheduleManager;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        quartzScheduleManager.scheduleAllJobs();
    }
}
