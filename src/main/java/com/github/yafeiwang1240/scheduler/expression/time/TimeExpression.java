package com.github.yafeiwang1240.scheduler.expression.time;

import com.github.yafeiwang1240.scheduler.expression.Expression;
import com.github.yafeiwang1240.scheduler.core.TimeDecoder;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class TimeExpression implements Expression, Serializable {

    private TimeUnit unit;

    private long space = TimeDecoder.IS_STOP_TIME;

    public TimeExpression() {

    }

    public TimeExpression(long space, TimeUnit unit) {
        this.space = space;
        this.unit = unit;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public long getSpace() {
        return space;
    }

    public void setSpace(long space) {
        this.space = space;
    }

    @Override
    public long getNextTime(long time) {
        return TimeDecoder.getNextTime(time, this);
    }
}
