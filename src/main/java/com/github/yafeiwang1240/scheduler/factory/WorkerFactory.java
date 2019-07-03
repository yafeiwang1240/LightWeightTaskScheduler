package com.github.yafeiwang1240.scheduler.factory;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.expression.Expression;

import java.util.Map;

public interface WorkerFactory {

    boolean addWorker(String name, String group, long startTime, Expression expression, Class<? extends Job> clazz, Map<?, ?> dataMap);

    boolean removeWorker(String name, String group);

}
