package com.nesterov.server.task.statistics;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;
import com.nesterov.core.request.ActionType;
import com.nesterov.server.statistics.StatisticsStorage;

import java.util.Optional;
import java.util.function.Consumer;

public class FileStatisticsFactory implements TaskFactory<Void,Void, RequestProperties> {
    private final StatisticsStorage<ActionType> statisticsStorage;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public FileStatisticsFactory(StatisticsStorage<ActionType> statisticsStorage,
                                 TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.statisticsStorage= Optional.of(statisticsStorage).get();
        this.taskExceptionFactory=Optional.of(taskExceptionFactory).get();
    }
    @Override
    public AbstractTask<Void, Void, RequestProperties> create(RequestProperties properties,
                                                              Consumer<Void> onProgress,
                                                              Consumer<Void> onComplete,
                                                              Consumer<Throwable> onFail) {
        return new FileStatisticTask(this.statisticsStorage,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
