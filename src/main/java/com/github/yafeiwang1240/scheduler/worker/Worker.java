package com.github.yafeiwang1240.scheduler.worker;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.annotation.EnableConcurrency;
import com.github.yafeiwang1240.scheduler.commons.Contains;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.handler.TaskMessageHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class Worker implements Runnable {

    private static class JobCache {
        protected Object instance;
        protected Method method;
        protected JobCache(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }
    }

    private Future<?> future;

    private boolean running;

    private JobCache cache;

    private Object[] lock = new Object[0];

    private JobExecutionContext context;

    private TaskMessageHandler<?, String> handler;

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
            init();
            cache.method.invoke(cache.instance, context);
            handler.invoke(String.format("开始时间: %d, 结束时间: %d, full-name: %s", t, System.currentTimeMillis(), context.getJobTrigger().getFullName()));
        } catch (Throwable throwable) {
            handler.onFail(t + ": " + throwable.getMessage());
        } finally {
            running = false;
        }
    }

    /**
     * 效率优化
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     */
    private void init() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        running = true;
        if (cache == null) {
            synchronized (lock) {
                if (cache == null) {
                    Class<? extends Job> job = context.getJobTrigger().getJobClass();
                    Method method = job.getMethod(Contains.EXECUTE, JobExecutionContext.class);
                    cache = new JobCache(job.newInstance(), method);
                }
            }
        }
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public boolean isRunning() {
        return running;
    }
}
