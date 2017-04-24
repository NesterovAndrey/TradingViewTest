package com.nesterov.client.task.requestType;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;
import com.nesterov.core.request.ActionType;

import java.util.Optional;
import java.util.function.Consumer;

public class RequestTypeTaskFactory implements TaskFactory<Void,Void,RequestProperties> {

    private final ActionType actionType;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public RequestTypeTaskFactory(ActionType actionType,TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.actionType = actionType;
        this.taskExceptionFactory= Optional.of(taskExceptionFactory).get();
    }
    @Override
    public AbstractTask<Void, Void,RequestProperties> create(RequestProperties properties,
                                                             Consumer<Void> onProgress,
                                                             Consumer<Void> onComplete,
                                                             Consumer<Throwable> onFail) {
        return new RequestTypeTask(actionType,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
