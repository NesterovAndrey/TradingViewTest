package com.nesterov.core.executions.task;

import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.util.Updatable;
//Интерфейс задачи из цепочки вызовов задач(ExecutionChain)
public interface Task<P,V> extends Updatable<P> {
    void invoke(ExecutionChain<V> chain, V requestProperties);
}
