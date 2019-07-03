package com.github.yafeiwang1240.scheduler.job;

import com.github.yafeiwang1240.scheduler.Job;

import java.io.Serializable;

public interface JobTrigger extends Serializable, Comparable<JobTrigger> {

    public enum TriggerState {
        NONE,
        NORMAL,
        PAUSED,
        COMPLETE,
        ERROR,
        BLOCKED
    }

    TriggerState getState();

    void updateState(TriggerState state);

    JobKey getJobKey();

    String getDescription();

    JobDataMap getJobDataMap();

    JobDetail getJobDetail();

    Class<? extends Job> getJobClass();

    String getFullName();

    long getNextTime();

    void compute();

}
