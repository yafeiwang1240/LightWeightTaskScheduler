package com.githup.yafeiwang1240.scheduler.exception;

public class JobExecutionException extends SchedulerException{
    public JobExecutionException() {
        super();
    }

    public JobExecutionException(String msg) {
        super(msg);
    }
    public JobExecutionException(Throwable throwable) {
        super(throwable);
    }

    public JobExecutionException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
