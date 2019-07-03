package com.github.yafeiwang1240.scheduler.exception;

public class SchedulerException extends Exception {
    public SchedulerException() {
        super();
    }

    public SchedulerException(String msg) {
        super(msg);
    }
    public SchedulerException(Throwable throwable) {
        super(throwable);
    }

    public SchedulerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
