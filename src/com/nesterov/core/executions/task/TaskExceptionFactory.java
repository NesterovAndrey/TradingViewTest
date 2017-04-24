package com.nesterov.core.executions.task;

public interface TaskExceptionFactory<V> {
    public TaskException create(Throwable e,V requestProperties);
}
