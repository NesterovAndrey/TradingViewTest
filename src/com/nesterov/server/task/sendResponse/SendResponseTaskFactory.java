package com.nesterov.server.task.sendResponse;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class SendResponseTaskFactory implements TaskFactory<Float,Void,RequestProperties> {

    private final int timeout;
    private final ExecutorService executorService;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public SendResponseTaskFactory(int timeout,ExecutorService executorService,TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.timeout=timeout;
        this.executorService= Optional.of(executorService).get();
        this.taskExceptionFactory=Optional.of(taskExceptionFactory).get();
    }
    @Override
    public AbstractTask<Float, Void,RequestProperties> create(RequestProperties properties, Consumer<Float> onProgress, Consumer<Void> onComplete, Consumer<Throwable> onFail) {
        return new SendResponse(this.timeout,executorService,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
