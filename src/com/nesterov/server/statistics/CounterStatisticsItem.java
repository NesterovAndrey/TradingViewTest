package com.nesterov.server.statistics;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//Хранит статистику по разным взаимодействиям
public class CounterStatisticsItem implements StatisticsItem<String,Integer> {
    private final Map<String,Optional<Integer>> propertis=new ConcurrentHashMap<>();
    @Override
    public StatisticsItem addTo(String key,Integer count) {
        //Пытаемся добавить значене к элементу по ключу.
        //Если ключа нет создаём новое значение по умолчанию
        Integer current=Optional.ofNullable(
                this.propertis.get(key))
                .orElse(
                        Optional.of(0))
                .get();
        Integer add=Optional.<Integer>of(count).get();
        this.propertis.put(key,Optional.<Integer>of(current+add));
        return null;
    }

    @Override
    public StatisticsItem removeFrom(String key,Integer count) {
        //Удаляем значене по ключу
        Integer current=Optional.ofNullable(this.propertis.get(key)).orElse(Optional.of(0)).get();
        Integer add=Optional.<Integer>of(count).get();
        this.propertis.put(key,Optional.<Integer>of(current-add));
        return this;
    }

    @Override
    public Integer get(String key) {

        return this.propertis.get(key).orElse(0);
    }

    @Override
    public StatisticsItem erase(String key) {
        this.propertis.remove(key);
        return this;
    }

    @Override
    public Set<String> getKeys() {
        return this.propertis.keySet();
    }
}
