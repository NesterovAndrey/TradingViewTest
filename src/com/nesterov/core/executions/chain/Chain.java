package com.nesterov.core.executions.chain;

//Цепочка вызово задач
public interface Chain<T> {
    /**
     * Вызывает следующую задачу из списка
     * @param properties
     */
    void doNext(T properties);
}
