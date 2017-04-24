package com.nesterov.core.executions.task;

import com.nesterov.core.executions.builder.ExecutionMap;
import com.nesterov.core.executions.chain.ExecutionChainBuilder;

public interface TaskExecutor<C> {
    /**
     * Возвращает ExecutionChainBuilder соответствующий значению переданному в параметре
     * @param command ключ для получения ExecutionChainBuilder
     * @param <T>
     * @param <V>
     * @return
     */
    <T,V>ExecutionChainBuilder<T,V> execution(C command);
    ExecutionMap getExecutionMap();
}
