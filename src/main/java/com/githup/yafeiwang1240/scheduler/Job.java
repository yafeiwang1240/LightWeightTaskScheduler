package com.githup.yafeiwang1240.scheduler;

import com.githup.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.githup.yafeiwang1240.scheduler.exception.JobExecutionException;

public interface Job {
    void execute(JobExecutionContext var1) throws JobExecutionException;
}
