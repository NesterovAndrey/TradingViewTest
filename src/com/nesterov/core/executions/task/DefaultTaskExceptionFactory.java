package com.nesterov.core.executions.task;


public class DefaultTaskExceptionFactory<V> implements TaskExceptionFactory<V> {
    @Override
    public TaskException create(Throwable e, Object requestProperties) {
        return new TaskException(e.getMessage());
    }
}
