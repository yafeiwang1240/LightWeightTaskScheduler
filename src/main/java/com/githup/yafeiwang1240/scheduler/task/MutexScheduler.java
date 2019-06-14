package com.githup.yafeiwang1240.scheduler.task;

import com.githup.yafeiwang1240.scheduler.Job;
import com.githup.yafeiwang1240.scheduler.commons.Contains;
import com.githup.yafeiwang1240.scheduler.expression.Expression;
import com.githup.yafeiwang1240.scheduler.factory.TaskBeanFactory;
import com.githup.yafeiwang1240.scheduler.factory.TaskFactory;
import com.githup.yafeiwang1240.scheduler.factory.WorkerBeanFactory;
import com.githup.yafeiwang1240.scheduler.factory.WorkerFactory;
import com.githup.yafeiwang1240.scheduler.handler.TaskMessageHandler;
import com.githup.yafeiwang1240.scheduler.worker.WorkerThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class MutexScheduler {

    private List<Info> loggerInfo = new ArrayList<>(Contains.DEFAULT_LOGGER_SIZE);

    private volatile int index = 0;

    private ExecutorService mainThreadExecutor;

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
        mainThreadExecutor = WorkerThreadPool.newSingleThreadExecutor();
        init();
    }

    private void init() {
        workerBeanFactory = new WorkerBeanFactory();
        ((WorkerBeanFactory) workerBeanFactory).setAddWorkerHandler(
                (_key, _value) -> ((TaskBeanFactory) taskBeanFactory).addWorker(_key, _value)
        );
        ((WorkerBeanFactory) workerBeanFactory).setRemoveWorkerHandler(
                (_key, _value) -> ((TaskBeanFactory) taskBeanFactory).removeWorker(_key)
        );

        taskBeanFactory = new TaskBeanFactory();
        ((TaskBeanFactory)taskBeanFactory).setRemoveWorkerHandler(
                (_key, _value) -> ((WorkerBeanFactory) workerBeanFactory).removeWorker(_key)
        );
        ((TaskBeanFactory) taskBeanFactory).setExecuteWorkerHandler((_key, _value) -> {
            if(_value.getHandler() == null) {
                _value.setHandler(new TaskMessageHandler<Object, String>() {
                    public Object invoke(String message) {
                        addLogInfo(true, message);
                        return null;
                    }
                    public void onFail(String message){
                        addLogInfo(false, message);
                    }
                    private void addLogInfo(boolean succeed, String message) {
                        Info info;
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
            taskThreadExecutor.execute(_value);
        });
        ((TaskBeanFactory) taskBeanFactory).setTrackerWorkerHandler(
                (_key, _value) ->  mainThreadExecutor.execute(_value)
        );
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
