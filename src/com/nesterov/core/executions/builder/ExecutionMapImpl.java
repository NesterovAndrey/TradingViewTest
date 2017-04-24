package com.nesterov.core.executions.builder;

import com.nesterov.core.executions.chain.ExecutionChainBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Мапиинг билдеров цепочек вызовов команд.
 * Хранит соответсвие ключа(типа команды) и цепочки вызова
 * @param <T> Ключ с которым будет ассоциироваться ExectutionChainBuilder
 * @param <V> ExecutionChainBuilder
 */
public class ExecutionMapImpl<T,V extends ExecutionChainBuilder> implements ExecutionMap<T,V> {
    private final Map<T,V> commandsMapping;
    public ExecutionMapImpl()
    {
        commandsMapping=new HashMap<>();
    }
    @Override
    public void map(T key, V object) {
        commandsMapping.put(key,object);
    }

    @Override
    public V get(T key) {
        return commandsMapping.get(key);
    }

    @Override
    public Boolean contains(T key) {
        return commandsMapping.containsKey(key);
    }

    @Override
    public Collection<T> getKeys() {
        return commandsMapping.keySet();
    }
}
