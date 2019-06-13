package com.githup.yafeiwang1240.scheduler.task;

import java.util.concurrent.TimeUnit;

public class Config {

    private String node;

    private String name;

    private String group;

    private int corePoolSize;

    private int maximumPoolSize;

    private long keepAliveTime;

    private int capacity;

    private TimeUnit unit = TimeUnit.MILLISECONDS;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        if(unit != null)
            this.unit = unit;
    }

    public void copy(Config _config) {
        setMaximumPoolSize(_config.getMaximumPoolSize());
        setCorePoolSize(_config.getCorePoolSize());
        setGroup(_config.getGroup());
        setName(_config.getName());
        setCapacity(_config.getCapacity());
        setKeepAliveTime(_config.getKeepAliveTime());
        setNode(_config.getNode());
        setUnit(_config.getUnit());
    }
}
