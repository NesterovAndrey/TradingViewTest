package com.nesterov.core.executions.task;

import com.nesterov.core.executions.builder.ExecutionMap;
import com.nesterov.core.executions.chain.ExecutionChainBuilder;
import com.nesterov.core.request.ActionType;

public class TaskExecutorImpl implements TaskExecutor<ActionType> {
    private final ExecutionMap executionMap;
    public TaskExecutorImpl(ExecutionMap executionMap)
    {
        this.executionMap = executionMap;
    }
    @Override
    public <T,V> ExecutionChainBuilder<T,V> execution(ActionType command) {
        return this.executionMap.get(command);
    }

    public ExecutionMap getExecutionMap() {
        return this.executionMap;
    }
}
