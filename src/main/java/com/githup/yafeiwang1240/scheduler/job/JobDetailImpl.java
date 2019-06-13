package com.githup.yafeiwang1240.scheduler.job;

import com.githup.yafeiwang1240.scheduler.Job;

import java.util.Map;

public class JobDetailImpl implements JobDetail {
    private JobKey jobKey;
    private String description;
    private Class<? extends Job> jobClass;
    private JobDataMap jobDataMap;

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public void putAllDataMap(Map<?, ?> map) {
        this.jobDataMap.putAll(map);
    }

    public String getFullName() {
        return jobKey.getFullName();
    }

    @Override
    public JobKey getJobKey() {
        return jobKey;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    @Override
    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    @Override
    public JobBuilder getJobBuilder() {
        JobBuilder b = JobBuilder.newJob()
                .ofType(getJobClass())
                .usingJobData(getJobDataMap())
                .withDescription(getDescription())
                .withIdentity(getJobKey());
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobDetail)) {
            return false;
        }

        JobDetail other = (JobDetail) obj;

        if(other.getJobKey() == null || getJobKey() == null)
            return false;

        if (!other.getJobKey().equals(getJobKey())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        JobKey key = getJobKey();
        return key == null ? 0 : getJobKey().hashCode();
    }
}
