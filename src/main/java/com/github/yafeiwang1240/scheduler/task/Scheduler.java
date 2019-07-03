package com.github.yafeiwang1240.scheduler.task;

import com.github.yafeiwang1240.scheduler.expression.Expression;
import com.github.yafeiwang1240.scheduler.Job;

import java.util.Map;

public interface Scheduler {

    boolean removeJob(String name, String group);

    boolean sumbitJob(String name, String group, long startTime, Expression expression, Class<? extends Job> clazz, Map<?, ?> dataMap);

    boolean start();

    boolean shutdown();
}
