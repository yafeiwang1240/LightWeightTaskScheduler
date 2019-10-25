package com.github.yafeiwang1240.scheduler;

import com.github.yafeiwang1240.scheduler.core.TimeDecoder;
import com.github.yafeiwang1240.scheduler.expression.cron.Cron;
import com.github.yafeiwang1240.scheduler.expression.cron.CronExpression;
import com.github.yafeiwang1240.scheduler.expression.time.TimeExpression;
import com.github.yafeiwang1240.scheduler.factory.SchedulerFactory;
import com.github.yafeiwang1240.scheduler.task.Scheduler;
import com.github.yafeiwang1240.scheduler.task.SchedulerBuilder;
import com.github.yafeiwang1240.scheduler.util.DateUtils;
import com.github.yafeiwang1240.scheduler.factory.SchedulerBeanFactory;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JobClient {

    private static SchedulerFactory schedulerFactory;

    private static final String ONLY_ONE_GROUP = "ONLY_ONE_GROUP";

    static {
        Scheduler scheduler = SchedulerBuilder.newSchedulerBuilder()
                .withCorePoolSize(10)
                .withGroup("Scheduler_Group")
                .withMaximumPoolSize(15)
                .withName("Scheduler")
                .withCapacity(30)
                .build();
        schedulerFactory = new SchedulerBeanFactory(scheduler);
    }

    /**
     * 五分钟后执行且执行一次
     * @param name
     * @param clazz
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, Class<? extends Job> clazz) throws ParseException{
        long startTime = System.currentTimeMillis() + 300000L;
        return submit(name, ONLY_ONE_GROUP, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, null);
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
     *
     * @param group
     * @param name [任务名称]
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
     * 执行一次定时任务
     * @param name
     * @param startTime
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        return submit(name, null, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, map);
    }

    /**
     * 执行一次定时任务
     * @param name
     * @param group
     * @param startTime
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String group, String startTime, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        return submit(name, group, startTime, TimeDecoder.IS_STOP_TIME, null, clazz, map);
    }

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) {
        return submit(name, group, System.currentTimeMillis(), space, unit, clazz, map);
    }

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param startTime
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     * @throws ParseException
     */
    public static boolean submit(String name, String group, String startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) throws ParseException {
        long start = DateUtils.toLong(DateUtils.toDate(startTime));
        return submit(name, group, start, space, unit, clazz, map);
    }

    /**
     * 重复执行定时任务
     * @param name
     * @param group
     * @param startTime
     * @param space
     * @param unit
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submit(String name, String group, long startTime, long space, TimeUnit unit, Class<? extends Job> clazz, Map<?, ?> map) {
        TimeExpression timeExpression = new TimeExpression(space, unit);
        return schedulerFactory.getScheduler().submitJob(name, group, startTime, timeExpression, clazz, map);
    }

    /**
     * cron表达式
     * @param name
     * @param group
     * @param cronExpress
     * @param clazz
     * @param map
     * @return
     */
    public static boolean submitCron(String name, String group, String cronExpress, Class<? extends Job> clazz, Map<?, ?> map) {
        Cron cron = new CronExpression(cronExpress);
        return schedulerFactory.getScheduler().submitJob(name, group, System.currentTimeMillis(), cron, clazz, map);
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
     * 删除任务
     * @param name
     * @param group
     * @return
     */
    public static boolean remove(String name, String group) {
        return schedulerFactory.getScheduler().removeJob(name, group);
    }

    /**
     * 启动
     * @return
     */
    public static boolean start() {
        return schedulerFactory.getScheduler().start();
    }

    /**
     * 停止调度
     * @return
     */
    public static boolean shutdown() {
        return schedulerFactory.getScheduler().shutdown();
    }
}