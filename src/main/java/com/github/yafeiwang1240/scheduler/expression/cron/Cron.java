package com.github.yafeiwang1240.scheduler.expression.cron;

import com.github.yafeiwang1240.scheduler.expression.Expression;
import com.github.yafeiwang1240.scheduler.expression.cron.analysis.CronField;

import java.util.List;

public abstract class Cron implements Expression {
    public abstract List<CronField> getCronField();
}
