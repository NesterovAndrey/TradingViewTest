package com.nesterov.server.task.statistics;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.request.ActionType;
import com.nesterov.server.statistics.StatisticsStorage;

import java.util.function.Consumer;

/**
 * Изменяет статистику скачивания файлов
 */
public class FileStatisticTask extends AbstractTask<Void,Void, RequestProperties> {
    private static int ADD_COUNT=1;
    private final StatisticsStorage<ActionType> statisticsStorage;
    protected FileStatisticTask(StatisticsStorage<ActionType> statisticsStorage,
                             Consumer<Void> onProgress,
                             Consumer<Void> onComplete,
                             Consumer<Throwable> onFail,
                             TaskExceptionFactory<RequestProperties> exceptionFactory) {
        super(onProgress, onComplete,onFail,exceptionFactory);
        this.statisticsStorage=statisticsStorage;
    }

    /**
     * Получает из параметров цепи название файла и увеличивает статстику
     */
    @Override
    public Void execute(ExecutionChain chain, RequestProperties requestProperties) throws Exception {
        this.statisticsStorage.getEvent(ActionType.FILE).addTo(requestProperties.get(StringConstants.DOWNLOAD_FILE_NAME),ADD_COUNT);
        return null;
    }
}
