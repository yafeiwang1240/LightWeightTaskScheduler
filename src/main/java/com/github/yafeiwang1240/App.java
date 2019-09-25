package com.github.yafeiwang1240;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.JobClient;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.exception.JobExecutionException;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronArray;
import com.github.yafeiwang1240.scheduler.util.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        test1();
    }

    public static class JobText implements Job {

        @Override
        public void execute(JobExecutionContext var1) throws JobExecutionException {
            System.out.println(DateUtils.toString(new Date()));
        }
    }

    public static void test1() {
        System.out.println( "Hello World!" );
        System.out.println( String.format("这是%s, 这是%s, 这是%d", "hello", "world", 111) );
        Map<String, Object> map = new HashMap<>();
        map.put("1", "这是测试1");
        map.put("2", "这是测试2");
        map.put("3", "这是测试3");
        map.put("4", "这是测试4");
        JobClient.start();

        try {
            JobClient.submitCron("test", "test", "18/5 14-15 1 1 7 * 2019", JobText.class, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
