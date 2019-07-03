package com.github.yafeiwang1240.scheduler.task;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.expression.Expression;

import java.util.Map;

public class SchedulerImpl implements Scheduler {

    private MutexScheduler mutexScheduler;

    private Config config;

    SchedulerImpl(Config config){
        this.config = config;
        mutexScheduler = new MutexScheduler(config);
    }

    @Override
    public String toString() {
        return config == null ? "" : config.getGroup()
                + "." + config.getName()
                + "." + config.getNode();
    }

    @Override
    public boolean removeJob(String name, String group) {
        return mutexScheduler.removeJob(name, group);
    }

    @Override
    public boolean sumbitJob(String name, String group, long startTime, Expression expression, Class<? extends Job> clazz, Map<?, ?> dataMap) {
        return mutexScheduler.sumbitJob(name, group, startTime, expression, clazz, dataMap);
    }

    @Override
    public boolean start() {
        return mutexScheduler.start();
    }

    @Override
    public boolean shutdown() {
        return mutexScheduler.shutdown();
    }
}
