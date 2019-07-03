package com.github.yafeiwang1240.scheduler.factory;

import com.github.yafeiwang1240.scheduler.task.Scheduler;

public class SchedulerBeanFactory implements SchedulerFactory {

    private Scheduler scheduler;

    public SchedulerBeanFactory() {

    }

    public SchedulerBeanFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler getScheduler() {
        if(scheduler != null) {
            return scheduler;
        }
        return null;
    }
}
