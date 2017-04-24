package com.nesterov.server.task.fileDataTask;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public class FileRequestTaskFactory implements TaskFactory<Float,Void, RequestProperties> {
    private final Path folder;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public FileRequestTaskFactory(Path folder,
                                  TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.folder=folder;
        this.taskExceptionFactory=taskExceptionFactory;
    }
    @Override
    public AbstractTask<Float, Void,RequestProperties> create(RequestProperties properties,
                                                              Consumer<Float> onProgress,
                                                              Consumer<Void> onComplete,
                                                              Consumer<Throwable> onFail) {
        return new FileRequestTask(this.folder,onProgress, onComplete, onFail,taskExceptionFactory);
    }
}
