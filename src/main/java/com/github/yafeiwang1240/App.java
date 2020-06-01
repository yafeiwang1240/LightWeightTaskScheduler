package com.github.yafeiwang1240;

import com.github.yafeiwang1240.scheduler.core.TimeDecoder;
import com.github.yafeiwang1240.scheduler.expression.cron.CronExpression;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CronExpression expression = new CronExpression("0 10 0 2 1 ? *");
        Long time = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        for (int i = 0; i < 100; i++) {
            time = TimeDecoder.getNextTime(time, expression);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
        }
    }

}
