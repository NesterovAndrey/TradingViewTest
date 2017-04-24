package com.nesterov.server.statistics;

import java.util.Set;

public interface StatisticsItem<T,V> {
    StatisticsItem<T,V> addTo(T key,V count);
    StatisticsItem<T,V> removeFrom(T key,V count);
    V get(T key);
    StatisticsItem<T,V> erase(T key);
    Set<T> getKeys();
}
