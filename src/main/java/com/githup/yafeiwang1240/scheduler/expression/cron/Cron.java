package com.githup.yafeiwang1240.scheduler.expression.cron;

import com.githup.yafeiwang1240.scheduler.expression.Expression;

public abstract class Cron implements Expression {
    abstract String getExpression();
}
