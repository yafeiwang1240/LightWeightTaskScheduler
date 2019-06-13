package com.githup.yafeiwang1240.scheduler.handler;

public interface TaskManageHandler<K, V> {
    void invoke(K key, V value);
}
