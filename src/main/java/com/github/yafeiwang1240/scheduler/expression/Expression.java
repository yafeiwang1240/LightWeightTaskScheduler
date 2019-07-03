package com.github.yafeiwang1240.scheduler.expression;

public interface Expression {
    long getNextTime(long time);
}
