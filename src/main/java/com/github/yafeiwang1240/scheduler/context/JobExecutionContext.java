package com.github.yafeiwang1240.scheduler.context;

import com.github.yafeiwang1240.scheduler.exception.SchedulerException;
import com.github.yafeiwang1240.scheduler.job.JobDataMap;
import com.github.yafeiwang1240.scheduler.job.JobTrigger;
import com.github.yafeiwang1240.scheduler.Job;

public interface JobExecutionContext {

    JobDataMap getDataMap(Class<? extends Job> clazz) throws SchedulerException;

    JobTrigger getJobTrigger();

}
