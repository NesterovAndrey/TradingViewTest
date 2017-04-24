package com.nesterov.server.task.fileResponse;

import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;
import com.nesterov.util.fileSystem.BytesLoaderFactory;

import java.util.Optional;
import java.util.function.Consumer;

public class WriteFileTaskFactory implements TaskFactory<Integer, Void, AsynchronousChannelRequestProperties>{
    private final BytesLoaderFactory bytesLoaderFactory;
    private final int bufferSize;
    private final int timeout;
    private final TaskExceptionFactory<AsynchronousChannelRequestProperties> taskExceptionFactory;
    public WriteFileTaskFactory(BytesLoaderFactory bytesLoaderFactory, int bufferSize,int timeout,
                                TaskExceptionFactory<AsynchronousChannelRequestProperties>taskExceptionFactory) {
        this.bytesLoaderFactory = Optional.of(bytesLoaderFactory).get();
        this.bufferSize = Optional.of(bufferSize).get();
        this.timeout=timeout;
        this.taskExceptionFactory=Optional.of(taskExceptionFactory).get();
    }

    @Override
    public AbstractTask<Integer, Void, AsynchronousChannelRequestProperties> create(AsynchronousChannelRequestProperties properties, Consumer<Integer> onProgress, Consumer<Void> onComplete, Consumer<Throwable> onFail) {
        return new WriteFileTask(this.bytesLoaderFactory,this.bufferSize,timeout,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
