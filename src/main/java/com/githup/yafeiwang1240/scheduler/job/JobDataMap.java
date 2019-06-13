package com.githup.yafeiwang1240.scheduler.job;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JobDataMap<K, V> implements Map<K, V>, Serializable {

    private Map<K, V> dataMap;

    public JobDataMap() {
        dataMap = new HashMap<>();
    }

    public JobDataMap(Map<? extends K, ? extends V> map) {
        dataMap = new HashMap<>();
        if(map != null && map.size() > 0)
            dataMap.putAll(map);
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return dataMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return dataMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return dataMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if(m != null && m.size() > 0)
            dataMap.putAll(m);
    }

    @Override
    public void clear() {
        dataMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return dataMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return dataMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return dataMap.entrySet();
    }
}
