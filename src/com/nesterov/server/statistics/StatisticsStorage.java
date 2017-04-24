package com.nesterov.server.statistics;

import java.util.Set;
import java.util.function.Supplier;

public interface StatisticsStorage<T> {
    Set<T> eventsSet();
    <C,V> StatisticsItem<C,V> getEvent(T key);
    <C,V> StatisticsItem<C,V> getEvent(T key, Supplier<StatisticsItem<C, V>> supplier);
}
