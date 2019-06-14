package com.githup.yafeiwang1240;

import com.githup.yafeiwang1240.scheduler.Job;
import com.githup.yafeiwang1240.scheduler.JobClient;
import com.githup.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.githup.yafeiwang1240.scheduler.exception.JobExecutionException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println( String.format("这是%s, 这是%s, 这是%d", "hello", "world", 111) );
        Map<String, Object> map = new HashMap<>();
        map.put("1", "这是测试1");
        map.put("2", "这是测试2");
        map.put("3", "这是测试3");
        map.put("4", "这是测试4");
        JobClient.start();

        JobClient.submit("test", "test", 1000, TimeUnit.MILLISECONDS, JobText.class, map);
    }

    public static class JobText implements Job {

        @Override
        public void execute(JobExecutionContext var1) throws JobExecutionException {
            System.out.println("调度完毕");
        }
    }
}
