package com.github.yafeiwang1240.scheduler;

import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.exception.JobExecutionException;

public interface Job {
    void execute(JobExecutionContext var1) throws JobExecutionException;
}
