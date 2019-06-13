package com.githup.yafeiwang1240.scheduler.expression.cron;

import com.githup.yafeiwang1240.scheduler.core.TimeDecoder;

import java.io.Serializable;

public class CronExpression extends Cron implements Serializable {

    private String expression;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public long getNextTime(long time) {
        return TimeDecoder.getNextTime(time, this);
    }
}
