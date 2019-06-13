package com.githup.yafeiwang1240.scheduler;

import com.githup.yafeiwang1240.scheduler.core.TimeDecoder;
import com.githup.yafeiwang1240.scheduler.expression.time.TimeExpression;
import com.githup.yafeiwang1240.scheduler.factory.SchedulerBeanFactory;
import com.githup.yafeiwang1240.scheduler.factory.SchedulerFactory;
import com.githup.yafeiwang1240.scheduler.task.Scheduler;
import com.githup.yafeiwang1240.scheduler.task.SchedulerBuilder;
import com.githup.yafeiwang1240.scheduler.util.DateUtils;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JobClient {

    private static SchedulerFactory schedulerFactory;

    static {
        Scheduler scheduler = SchedulerBuilder.newSchedulerBuilder()
                .withCorePoolSize(2)
                .withGroup("Scheduler_Group")
                .withMaximumPoolSize(5)
                .withName("Scheduler")
                .build();
        schedulerFactory = new SchedulerBeanFactory(scheduler);
    }

    /**
     *  执行一次定时任务
     * @param name
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String startTime, Class<? extends Job> clazz) throws ParseException{
        return submit(name, null, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, null);
    }

    /**
     *  执行一次定时任务
     * @param group
     * @param name
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, String startTime, Class<? extends Job> clazz) throws ParseException {
        return submit(name, group, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, null);
    }

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param space
     * @param unit
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, long space, TimeUnit unit, Class<? extends Job> clazz) {
        return submit(name, group, System.currentTimeMillis(), space, unit, clazz, null);
    }

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param space
     * @param unit
     * @param clazz [job class]
     * @return
     */
    public static boolean submit(String name, String group, String startTime, long space, TimeUnit unit, Class<? extends Job> clazz) throws ParseException  {
        return submit(name, group, startTime, space, unit, clazz, null);
    }

    /**
     *  执行一次定时任务
     * @param name
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @param map
     * @return
     */
    public static boolean submit(String name, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        return submit(name, null, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, map);
    }

    /**
     *  执行一次定时任务
     * @param group
     * @param name
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param clazz [job class]
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        return submit(name, group, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, map);
    }

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param space
     * @param unit
     * @param clazz [job class]
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) {
        return submit(name, group, System.currentTimeMillis(), space, unit, clazz, map);
    }

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param startTime [yyyy-MM-dd HH:mm:ss]
     * @param space
     * @param unit
     * @param clazz [job class]
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, String startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        long start = DateUtils.toLong(DateUtils.toDate(startTime));
        return submit(name, group, start, space, unit, clazz, map);
    }

    /**
     *  重复执行定时任务
     * @param name
     * @param group
     * @param startTime [long]
     * @param space
     * @param unit
     * @param clazz [job class]
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) {
        TimeExpression timeExpression = new TimeExpression(space, unit);
        return schedulerFactory.getScheduler().sumbitJob(name, group, startTime, timeExpression, clazz, map);
    }

    /**
     * 删除任务
     * @param name
     * @return
     */
    public static boolean remove(String name) {
        return remove(name, null);
    }

    /**
     *  删除任务
     * @param name
     * @param group
     * @return
     */
    public static boolean remove(String name, String group) {
        return schedulerFactory.getScheduler().removeJob(name, group);
    }

    public static boolean start() {
        return schedulerFactory.getScheduler().start();
    }

    public static boolean shutdown() {
        return schedulerFactory.getScheduler().shutdown();
    }
}