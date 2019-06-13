package com.githup.yafeiwang1240.scheduler.task;

import com.githup.yafeiwang1240.scheduler.Job;
import com.githup.yafeiwang1240.scheduler.commons.Contains;
import com.githup.yafeiwang1240.scheduler.expression.Expression;
import com.githup.yafeiwang1240.scheduler.factory.TaskBeanFactory;
import com.githup.yafeiwang1240.scheduler.factory.TaskFactory;
import com.githup.yafeiwang1240.scheduler.factory.WorkerBeanFactory;
import com.githup.yafeiwang1240.scheduler.factory.WorkerFactory;
import com.githup.yafeiwang1240.scheduler.handler.TaskManageHandler;
import com.githup.yafeiwang1240.scheduler.handler.TaskMessageHandler;
import com.githup.yafeiwang1240.scheduler.worker.Worker;
import com.githup.yafeiwang1240.scheduler.worker.WorkerThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class MutexScheduler {

    private List<Info> loggerInfo = new ArrayList<>(Contains.DEFAULT_LOGGER_SIZE);

    private volatile int index = 0;

    private ThreadPoolExecutor mainThreadExecutor = WorkerThreadPool.newSignalThreadExecutor();

    private ThreadPoolExecutor taskThreadExecutor;

    private WorkerFactory workerBeanFactory;

    private TaskFactory taskBeanFactory;

    public MutexScheduler() {
        this(null);
    }

    public MutexScheduler(Config config) {
        if(config == null) {
            taskThreadExecutor = WorkerThreadPool.newThreadPoolExecutor();
        }else {
            taskThreadExecutor = WorkerThreadPool.newThreadPoolExecutor(
                    config.getCorePoolSize(), config.getMaximumPoolSize(),
                    config.getKeepAliveTime(), config.getUnit(),
                    config.getCapacity());
        }
        init();
    }

    private void init() {
        workerBeanFactory = new WorkerBeanFactory();
        ((WorkerBeanFactory) workerBeanFactory).setAddWorkerHandler(new TaskManageHandler<String, Worker>() {
            @Override
            public void invoke(String key, Worker value) {
                ((TaskBeanFactory) taskBeanFactory).addWorker(key, value);
            }
        });
        ((WorkerBeanFactory) workerBeanFactory).setRemoveWorkerHandler(new TaskManageHandler<String, Worker>() {
            @Override
            public void invoke(String key, Worker value) {
                ((TaskBeanFactory) taskBeanFactory).removeWorker(key);
            }
        });


        taskBeanFactory = new TaskBeanFactory();
        ((TaskBeanFactory)taskBeanFactory).setRemoveWorkerHandler(new TaskManageHandler<String, Worker>() {
            @Override
            public void invoke(String key, Worker value) {
                ((WorkerBeanFactory) workerBeanFactory).removeWorker(key);
            }
        });
        ((TaskBeanFactory) taskBeanFactory).setExecuteWorkerHandler(new TaskManageHandler<String, Worker>() {
            @Override
            public void invoke(String key, Worker value) {
                if(value.getHandler() == null) {
                    value.setHandler(new TaskMessageHandler<Object, String>() {
                        public Object invoke(String message) {
                            addLogInfo(true, message);
                            return null;
                        }
                        public void onFail(String message){
                            addLogInfo(false, message);
                        }

                        private void addLogInfo(boolean succeed, String message) {
                            Info info = null;
                            if(index >= loggerInfo.size()) {
                                info = new Info(succeed, message);
                                loggerInfo.add(info);
                            }else {
                                info = loggerInfo.get(index);
                                info.setInfo(message);
                                info.setSucceed(succeed);
                            }
                            index = index + 1 < Contains.DEFAULT_LOGGER_SIZE ? index + 1 : 0;
                        }
                    });
                }
                taskThreadExecutor.execute(value);
            }
        });
        ((TaskBeanFactory) taskBeanFactory).setTrackerWorkerHandler(new TaskManageHandler<String, TaskBeanFactory.TaskTracker>() {
            @Override
            public void invoke(String key, TaskBeanFactory.TaskTracker value) {
                mainThreadExecutor.execute(value);
            }
        });
    }

    public boolean removeJob(String name, String group) {
        return workerBeanFactory.removeWorker(name, group);
    }

    public boolean sumbitJob(String name, String group, long startTime,Expression expression,
                             Class<? extends Job> clazz, Map<?, ?> dataMap) {
        return workerBeanFactory.addWorker(name, group, startTime, expression, clazz, dataMap);
    }

    public boolean start() {
        return taskBeanFactory.start();
    }

    public boolean shutdown() {
        return taskBeanFactory.shutdown();
    }

    public static class Info {

        private boolean succeed;
        private String info;

        private Info(boolean succeed, String info){
            setSucceed(succeed);
            setInfo(info);
        }

        public boolean isSucceed() {
            return succeed;
        }

        private void setSucceed(boolean succeed) {
            this.succeed = succeed;
        }

        public String getInfo() {
            return info;
        }

        private void setInfo(String info) {
            this.info = info;
        }
    }
}
