package com.githup.yafeiwang1240.scheduler.factory;

import com.githup.yafeiwang1240.scheduler.expression.Expression;
import com.githup.yafeiwang1240.scheduler.Job;

import java.util.Map;

public interface WorkerFactory {

    boolean addWorker(String name, String group, long startTime, Expression expression, Class<? extends Job> clazz, Map<?, ?> dataMap);

    boolean removeWorker(String name, String group);

}
