package com.githup.yafeiwang1240.scheduler.job;

import com.githup.yafeiwang1240.scheduler.Job;

public interface JobDetail {

    JobKey getJobKey();

    String getDescription();

    Class<? extends Job> getJobClass();

    String getFullName();

    JobDataMap getJobDataMap();

    JobBuilder getJobBuilder();
}
