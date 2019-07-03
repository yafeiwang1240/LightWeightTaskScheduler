package com.github.yafeiwang1240.scheduler.job;

import com.github.yafeiwang1240.scheduler.Job;

public class JobBuilder {

    private JobKey key;
    private String description;
    private Class<? extends Job> jobClass;
    private JobDataMap jobDataMap = new JobDataMap();

    protected JobBuilder() {
    }

    public static JobBuilder newJob() {
        return new JobBuilder();
    }

    public static JobBuilder newJob(Class <? extends Job> jobClass) {
        JobBuilder b = new JobBuilder();
        b.ofType(jobClass);
        return b;
    }

    public JobDetail build() {
        JobDetailImpl job = new JobDetailImpl();

        job.setJobClass(jobClass);
        job.setDescription(description);
        if(key == null)
            key = new JobKey(Key.createUniqueName(null), null);
        job.setJobKey(key);

        if(!jobDataMap.isEmpty())
            job.setJobDataMap(jobDataMap);

        return job;
    }

    public JobBuilder withIdentity(String name) {
        key = new JobKey(name, null);
        return this;
    }

    public JobBuilder withIdentity(String name, String group) {
        key = new JobKey(name, group);
        return this;
    }

    public JobBuilder withIdentity(JobKey jobKey) {
        this.key = jobKey;
        return this;
    }

    public JobBuilder withDescription(String jobDescription) {
        this.description = jobDescription;
        return this;
    }

    public JobBuilder ofType(Class <? extends Job> jobClazz) {
        this.jobClass = jobClazz;
        return this;
    }

    public JobBuilder usingJobData(String dataKey, Object value) {
        jobDataMap.put(dataKey, value);
        return this;
    }

    public JobBuilder usingJobData(JobDataMap newJobDataMap) {
        jobDataMap.putAll(newJobDataMap);
        return this;
    }

    public JobBuilder setJobData(JobDataMap newJobDataMap) {
        jobDataMap = newJobDataMap;
        return this;
    }
}
