package com.github.yafeiwang1240;

import com.github.yafeiwang1240.scheduler.Job;
import com.github.yafeiwang1240.scheduler.JobClient;
import com.github.yafeiwang1240.scheduler.context.JobExecutionContext;
import com.github.yafeiwang1240.scheduler.core.TimeDecoder;
import com.github.yafeiwang1240.scheduler.exception.JobExecutionException;
import com.github.yafeiwang1240.scheduler.expression.cron.CronExpression;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        Thread.sleep(10000);
        JobClient.start();
        JobClient.submitCron("runJob", "test", "0 0 0 * * ? *", RunJob.class, null);
        JobClient.submitCron("runJob2", "test", "0 0 0 * * ? *", RunJob2.class, null);
        JobClient.submitRunThenCron("runJob3", "test", "0 0 0 * * ? *", RunJob3.class, null);
        Thread.sleep(10000);
        JobClient.remove("runJob", "test");

        JobClient.submitCron("runJob", "test", "0 * * * * ? *", RunJob.class, null);
    }

    public static class RunJob implements Job {

        @Override
        public void execute(JobExecutionContext var1) throws JobExecutionException {
            System.out.println("RunJob1: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

    public static class RunJob2 implements Job {

        @Override
        public void execute(JobExecutionContext var1) throws JobExecutionException {
            System.out.println("RunJob2: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

    public static class RunJob3 implements Job {

        @Override
        public void execute(JobExecutionContext var1) throws JobExecutionException {
            System.out.println("RunJob3: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

}
