package com.nesterov.client;

import com.nesterov.core.executions.task.TaskExecutor;

public class FileTransferClient<T> {
    private TaskExecutor<T> taskExecutor;
    public FileTransferClient(TaskExecutor<T> taskExecutor)
    {
        this.taskExecutor=taskExecutor;
    }
    public TaskExecutor<T> getTaskExecutor() {
        return taskExecutor;
    }
}
