package com.githup.yafeiwang1240.scheduler.task;

public class SchedulerBuilder {

    private Config config;

    private SchedulerBuilder() {
        config = new Config();
    }

    public static SchedulerBuilder newSchedulerBuilder() {
        return new SchedulerBuilder();
    }

    public SchedulerBuilder withName(String name) {
        config.setName(name);
        return this;
    }

    public SchedulerBuilder withGroup(String group) {
        config.setGroup(group);
        return this;
    }

    public SchedulerBuilder withCorePoolSize(int corePoolSize) {
        config.setCorePoolSize(corePoolSize);
        return this;
    }

    public SchedulerBuilder withMaximumPoolSize(int maximumPoolSize) {
        config.setMaximumPoolSize(maximumPoolSize);
        return this;
    }

    public SchedulerBuilder withCapacity(int capacity) {
        config.setCapacity(capacity);
        return this;
    }


    public Scheduler build() {
        Config _config = new Config();
        _config.copy(config);
        SchedulerImpl scheduler = new SchedulerImpl(_config);
        return scheduler;
    }

}
