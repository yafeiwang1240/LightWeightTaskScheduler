package com.github.yafeiwang1240.scheduler.expression.cron;

import com.github.yafeiwang1240.scheduler.core.TimeDecoder;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronField;
import com.github.yafeiwang1240.scheduler.expression.cron.util.CronUtils;

import java.io.Serializable;
import java.util.List;

public class CronExpression extends Cron implements Serializable {

    private String expression;
    private List<CronField> cronFields;

    public CronExpression(String expression) {
        this.expression = expression;
        cronFields = CronUtils.convertCron(expression);
    }

    @Override
    public long getNextTime(long time) {
        return TimeDecoder.getNextTime(time, this);
    }

    @Override
    public List<CronField> getCronField() {
        return cronFields;
    }
}
