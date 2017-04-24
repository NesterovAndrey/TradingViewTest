package com.nesterov.client.task.requestType;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.request.ActionType;
import java.util.function.Consumer;

/**
 * Заполняет тип запроса в параметрах
 */
public class RequestTypeTask extends AbstractTask<Void,Void,RequestProperties> {
    private final ActionType actionType;
    protected RequestTypeTask(ActionType actionType,
                           Consumer<Void> onProgress,
                           Consumer<Void> onComplete,
                           Consumer<Throwable> onFail,
                           TaskExceptionFactory<RequestProperties> exceptionFactory)
    {
        super(onProgress, onComplete,onFail,exceptionFactory);
        this.actionType = actionType;
    }

    @Override
    public Void execute(ExecutionChain<RequestProperties> chain, RequestProperties requestProperties) throws Exception {
        requestProperties.put(StringConstants.TYPE, actionType.toString());
        return null;
    }
}
