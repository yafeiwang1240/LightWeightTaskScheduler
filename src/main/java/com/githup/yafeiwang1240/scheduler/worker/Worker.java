package com.githup.yafeiwang1240.scheduler.worker;

import com.githup.yafeiwang1240.scheduler.Job;
import com.githup.yafeiwang1240.scheduler.annotation.EnableConcurrency;
import com.githup.yafeiwang1240.scheduler.commons.Contains;
import com.githup.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.githup.yafeiwang1240.scheduler.handler.TaskMessageHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Worker implements Runnable {

    private JobExecutionContext context;

    private TaskMessageHandler<?, String> handler;

    private boolean running = false;

    public JobExecutionContext getContext() {
        return context;
    }

    public void setContext(JobExecutionContext context) {
        this.context = context;
    }

    public TaskMessageHandler<?, String> getHandler() {
        return handler;
    }

    public void setHandler(TaskMessageHandler<?, String> handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        long t = System.currentTimeMillis();
        try {
            Class<? extends Job> job = context.getJobTrigger().getJobClass();
            // 判断是否允许并发
            if(job.getAnnotation(EnableConcurrency.class) != null) {
                running = true;
            }
            Method method = job.getMethod(Contains.EXECUTE, JobExecutionContext.class);
            try {
                method.invoke(job.newInstance(), context);
                handler.invoke(String.format("开始时间: %d, 结束时间: %d, full-name: %s", t, System.currentTimeMillis(), context.getJobTrigger().getFullName()));
            } catch (InstantiationException | IllegalAccessException e) {
                handler.onFail(t + ": " + e.getMessage());
            } catch (IllegalArgumentException | InvocationTargetException e) {
                handler.onFail(t + ": " + e.getMessage());
            }
        } catch (NoSuchMethodException | SecurityException e) {
            handler.onFail(t + ": " + e.getMessage());
        } finally {
            running = false;
        }
    }
    public boolean isRunning() {
        return running;
    }
}
