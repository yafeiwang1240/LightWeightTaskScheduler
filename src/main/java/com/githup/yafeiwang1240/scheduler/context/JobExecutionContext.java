package com.githup.yafeiwang1240.scheduler.context;

import com.githup.yafeiwang1240.scheduler.Job;
import com.githup.yafeiwang1240.scheduler.exception.SchedulerException;
import com.githup.yafeiwang1240.scheduler.job.JobDataMap;
import com.githup.yafeiwang1240.scheduler.job.JobTrigger;

public interface JobExecutionContext {

    JobDataMap getDataMap(Class<? extends Job> clazz) throws SchedulerException;

    JobTrigger getJobTrigger();

}
