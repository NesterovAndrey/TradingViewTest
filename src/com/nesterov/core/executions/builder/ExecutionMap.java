package com.nesterov.core.executions.builder;

import com.nesterov.core.executions.chain.ExecutionChainBuilder;

import java.util.Collection;

public interface ExecutionMap<T,V extends ExecutionChainBuilder> {
    /**
     * Добавляет новый ExecutionChainBuilder
     * @param key ключ
     * @param object ExecutionChainBuilder
     */
    void map(T key, V object);

    /**
     * Возвращает ExecutionChainBuilder по ключу
     * @param key ключ соответствующий одному из ExecutionChainBuilder
     * @return
     */
    V get(T key);

    /**
     * Проверяет содержит ли класс объект с переданным ключе
     * @param key ключ
     * @return
     */
    Boolean contains(T key);

    /**
     * Возвращает список ключей
     * @return
     */
    Collection<T> getKeys();
}
