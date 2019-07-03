package com.github.yafeiwang1240.scheduler.factory;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.commons.Contains;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContextImpl;
import com.github.yafeiwang1240.scheduler.expression.Expression;
import com.github.yafeiwang1240.scheduler.handler.TaskManageHandler;
import com.github.yafeiwang1240.scheduler.job.*;
import com.github.yafeiwang1240.scheduler.worker.Worker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerBeanFactory implements WorkerFactory {

    private Map<String, Worker> workerMap = new ConcurrentHashMap<>();

    private TaskManageHandler<String, Worker> addWorkerHandler;

    private TaskManageHandler<String, Worker> removeWorkerHandler;

    public void setAddWorkerHandler(TaskManageHandler<String, Worker> addWorkerHandler) {
        this.addWorkerHandler = addWorkerHandler;
    }

    public void setRemoveWorkerHandler(TaskManageHandler<String, Worker> removeWorkerHandler) {
        this.removeWorkerHandler = removeWorkerHandler;
    }

    @Override
    public boolean addWorker(String name, String group, long startTime, Expression expression, Class<? extends Job> clazz, Map<?, ?> dataMap) {
        String fullName = Contains.getFullName(name, group);
        Worker worker = workerMap.get(fullName);
        if(worker == null) {
            JobKey jobKey = new JobKey(name, group);
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setDescription(String.format("新建任务: %s, name: %s, group: %s", clazz.toString(), jobKey.getName(), jobKey.getGroup()));
            jobDetail.setJobClass(clazz);
            jobDetail.setJobKey(jobKey);
            JobDataMap jobDataMap = new JobDataMap(dataMap);
            jobDetail.setJobDataMap(jobDataMap);
            JobTriggerImpl jobTrigger = new JobTriggerImpl();
            jobTrigger.updateState(JobTrigger.TriggerState.NONE);
            jobTrigger.setStartTime(startTime);
            jobTrigger.setExpression(expression);
            jobTrigger.setJobDetail(jobDetail);
            jobTrigger.compute();
            JobExecutionContextImpl jobExecutionContext = new JobExecutionContextImpl(jobTrigger);
            worker = new Worker();
            worker.setContext(jobExecutionContext);
            addWorkerHandler.invoke(fullName, worker);
        } else {
            JobExecutionContext jobExecutionContext = worker.getContext();
            JobTrigger jobTrigger = jobExecutionContext.getJobTrigger();
            ((JobTriggerImpl) jobTrigger).setExpression(expression);
            ((JobTriggerImpl) jobTrigger).setStartTime(startTime);
            jobTrigger.compute();
            JobDetail jobDetail = jobTrigger.getJobDetail();
            JobKey jobKey = jobDetail.getJobKey();
            ((JobDetailImpl) jobDetail).setDescription(String.format("更新任务: %s, name: %s, group: %s", clazz.toString(), jobKey.getName(), jobKey.getGroup()));
            ((JobDetailImpl) jobDetail).setJobClass(clazz);
            ((JobDetailImpl) jobDetail).putAllDataMap(dataMap);
        }
        return true;
    }

    @Override
    public boolean removeWorker(String name, String group) {
        String fullName = Contains.getFullName(name, group);
        Worker worker = removeWorker(fullName);
        if(worker == null) {
            return false;
        }
        removeWorkerHandler.invoke(fullName, worker);
        return true;
    }

    public Worker removeWorker(String fullName) {
        Worker worker = workerMap.remove(fullName);
        return worker;
    }

}
