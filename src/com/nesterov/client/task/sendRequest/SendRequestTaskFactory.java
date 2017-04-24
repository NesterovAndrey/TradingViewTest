package com.nesterov.client.task.sendRequest;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class SendRequestTaskFactory implements TaskFactory<Float,Boolean,RequestProperties> {
    private final int timeout;
    private final ExecutorService executorService;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public SendRequestTaskFactory(int timeout, ExecutorService executorService,TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.timeout=timeout;
        this.executorService=executorService;
        this.taskExceptionFactory=taskExceptionFactory;
    }
    @Override
    public AbstractTask<Float, Boolean,RequestProperties> create(RequestProperties properties,
                                                                 Consumer<Float> onProgress,
                                                                 Consumer<Boolean> onComplete,
                                                                 Consumer<Throwable>onFail) {
        return new SendRequestTask(this.timeout,this.executorService,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
