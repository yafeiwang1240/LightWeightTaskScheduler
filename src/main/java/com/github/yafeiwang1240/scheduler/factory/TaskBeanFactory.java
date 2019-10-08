package com.github.yafeiwang1240.scheduler.factory;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.annotation.Coverage;
import com.github.yafeiwang1240.scheduler.annotation.UnableConcurrency;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.core.TimeDecoder;
import com.github.yafeiwang1240.scheduler.job.JobTrigger;
import com.github.yafeiwang1240.scheduler.worker.Worker;
import com.github.yafeiwang1240.obrien.uitls.IOUtils;
import com.github.yafeiwang1240.scheduler.handler.TaskManageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskBeanFactory implements TaskFactory {

    public enum TaskState {
        NEW,
        RUNNABLE,
        RUNNING,
        WAITING,
        SHUTDOWN,
    }

    private TaskState state = TaskState.NEW;

    private Map<String, Worker> workerMap = new ConcurrentHashMap<>();

    private TaskManageHandler<String, Worker> executeWorkerHandler;

    private TaskManageHandler<String, TaskTracker> trackerWorkerHandler;

    private TaskManageHandler<String, Worker> removeWorkerHandler;

    private TaskTracker taskTracker;

    public void setExecuteWorkerHandler(TaskManageHandler<String, Worker> executeWorkerHandler) {
        this.executeWorkerHandler = executeWorkerHandler;
    }

    public void setTrackerWorkerHandler(TaskManageHandler<String, TaskTracker> trackerWorkerHandler) {
        this.trackerWorkerHandler = trackerWorkerHandler;
    }

    public void setRemoveWorkerHandler(TaskManageHandler<String, Worker> removeWorkerHandler) {
        this.removeWorkerHandler = removeWorkerHandler;
    }

    public TaskState getState() {
        return state;
    }

    public boolean removeWorker(String fullName, Worker worker) {
        removeWorker(fullName);
        if(worker == null) {
            return false;
        }
        removeWorkerHandler.invoke(fullName, worker);
        return true;
    }

    public boolean executeWorker(String fullName, Worker worker) {

        if (worker.isRunning()) {
            Class<? extends Job> clazz = worker.getContext().getJobTrigger().getJobClass();
            // 不允许并发
            if (clazz.isAnnotationPresent(UnableConcurrency.class)) {
                return false;
            }
            // 覆盖运行
            if (clazz.isAnnotationPresent(Coverage.class)) {
                worker.getFuture().cancel(true);
            }
        }
        executeWorkerHandler.invoke(fullName, worker);
        worker.getContext().getJobTrigger().compute();
        return true;
    }

    public Worker removeWorker(String fullName) {
        Worker worker = workerMap.remove(fullName);
        return worker;
    }

    public void addWorker(String fullName, Worker worker) {
        workerMap.put(fullName, worker);
    }

    @Override
    public synchronized boolean start() {
        // 重复启动
        if(!(state == TaskState.NEW || state == TaskState.SHUTDOWN)) {
            return false;
        }
        if(taskTracker == null) {
            synchronized (this) {
                if(taskTracker == null) {
                    taskTracker = new TaskTracker();
                }
            }
        }
        state = TaskState.RUNNABLE;
        taskTracker.ready();
        trackerWorkerHandler.invoke("start", taskTracker);
        state = TaskState.RUNNING;
        return true;
    }

    @Override
    public synchronized boolean shutdown() {
        // 已关闭 或者 未启动
        if(state == TaskState.NEW || state == TaskState.SHUTDOWN) {
            return false;
        }
        taskTracker.stop();
        state = TaskState.SHUTDOWN;
        return true;
    }

    public class TaskTracker implements Runnable {
        private boolean exit = false;
        private int sum = 0;
        @Override
        public void run() {
            Map<String, Worker> removes = null;
            Map<String, Worker> executes = null;
            exit = false;
            while(!exit) {
                try {
                    long now = System.currentTimeMillis();
                    for(Map.Entry<String, Worker> work : workerMap.entrySet()) {
                        String key = work.getKey();
                        Worker worker = work.getValue();
                        JobExecutionContext context = worker.getContext();
                        JobTrigger jobTrigger = context.getJobTrigger();
                        long nextTime = jobTrigger.getNextTime();
                        // 使用创建 防止任务稀疏
                        if(nextTime == TimeDecoder.IS_STOP_TIME) {
                            if(removes == null) {
                                removes = new HashMap<>();
                            }
                            removes.put(key, worker);
                        } else if(now >= nextTime) {
                            if(executes == null) {
                                executes = new HashMap<>();
                            }
                            executes.put(key, worker);
                        }
                    }
                    // remove
                    if(removes != null && removes.size() > 0) {
                        for(Map.Entry<String, Worker> work : removes.entrySet()) {
                            String key = work.getKey();
                            Worker worker = work.getValue();
                            removeWorker(key, worker);
                        }
                        removes.clear();
                    }

                    // execute
                    if(executes !=  null && executes.size() > 0) {
                        for(Map.Entry<String, Worker> work : executes.entrySet()) {
                            String key = work.getKey();
                            Worker worker = work.getValue();
                            executeWorker(key, worker);
                            IOUtils.sleep(1);
                        }
                        executes.clear();
                    } else {
                        IOUtils.sleep(1);
                    }
                } catch (Throwable throwable) { }
            }
        }
        private void stop() {
            exit = true;
        }
        private void ready() {
            exit = false;
        }
    }

}
