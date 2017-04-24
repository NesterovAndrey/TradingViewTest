package com.nesterov.server.statistics;

import com.nesterov.core.request.ActionType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

//Класс управляющий различными инстансами классов статистики
public class StatisticsStorageImpl implements StatisticsStorage<ActionType>{
    private final Map<ActionType,StatisticsItem> events=new ConcurrentHashMap<>();

    @Override
    public Set<ActionType> eventsSet() {
        return this.events.keySet();
    }

    @Override
    public <C,V> StatisticsItem<C,V> getEvent(ActionType key) {
        return getEvent(key, () -> (StatisticsItem<C, V>)new CounterStatisticsItem());
    }

    @Override
    public <C, V> StatisticsItem<C, V> getEvent(ActionType key, Supplier<StatisticsItem<C, V>> supplier) {
        StatisticsItem<C, V> item=Optional.ofNullable(this.events.get(key)).orElseGet(supplier);
       this.events.put(key,item);
       return item;
    }
}
