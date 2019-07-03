package com.github.yafeiwang1240.scheduler.expression.cron;

import com.github.yafeiwang1240.scheduler.expression.Expression;

public abstract class Cron implements Expression {
    abstract String getExpression();
}
