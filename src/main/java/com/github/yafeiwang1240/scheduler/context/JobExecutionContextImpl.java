package com.github.yafeiwang1240.scheduler.context;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.exception.SchedulerException;
import com.github.yafeiwang1240.scheduler.job.JobDataMap;
import com.github.yafeiwang1240.scheduler.job.JobTrigger;

public class JobExecutionContextImpl implements JobExecutionContext {

    private JobTrigger jobTrigger;

    public JobExecutionContextImpl(JobTrigger jobTrigger) {
        this.jobTrigger = jobTrigger;
    }

    public void setJobTrigger(JobTrigger jobTrigger) {
        this.jobTrigger = jobTrigger;
    }

    @Override
    public JobDataMap getDataMap(Class<? extends Job> clazz) throws SchedulerException {
        JobDataMap jobDataMap = null;
        if(clazz == jobTrigger.getJobClass()) {
            jobDataMap = jobTrigger.getJobDataMap();
        } else {
            throw new SchedulerException("不同的任务");
        }
        return jobDataMap;
    }

    @Override
    public JobTrigger getJobTrigger() {
        return jobTrigger;
    }
}
