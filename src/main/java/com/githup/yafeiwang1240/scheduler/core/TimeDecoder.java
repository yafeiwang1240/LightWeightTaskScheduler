package com.githup.yafeiwang1240.scheduler.core;

import com.githup.yafeiwang1240.scheduler.expression.cron.Cron;
import com.githup.yafeiwang1240.scheduler.expression.time.TimeExpression;

public class TimeDecoder {

    public static final long IS_STOP_TIME = -1L;

    public static final Long IS_DEFAULT_TIME = -2L;

    public static long getNextTime(long time, TimeExpression dto) {

        if(dto.getSpace() < 0) {
            return IS_STOP_TIME;
        }

        long space = dto.getUnit().toMillis(dto.getSpace());
        return time + space;
    }

    public static long getNextTime(long time, Cron cron) {
        return IS_STOP_TIME;
    }
}
