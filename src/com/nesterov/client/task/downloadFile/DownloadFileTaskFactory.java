package com.nesterov.client.task.downloadFile;


import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;
import com.nesterov.util.fileSystem.FileStreamFileWriterFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public class DownloadFileTaskFactory implements TaskFactory<Integer,Boolean, AsynchronousChannelRequestProperties> {

    private final Path path;
    private int timeout;
    private int bufferSize;
    private final TaskExceptionFactory<AsynchronousChannelRequestProperties> taskExceptionFactory;
    public DownloadFileTaskFactory(Path path, int timeout, int bufferSize,TaskExceptionFactory<AsynchronousChannelRequestProperties> taskExceptionFactory)
    {
        this.path=path;
        this.timeout=timeout;
        this.bufferSize=bufferSize;
        this.taskExceptionFactory=taskExceptionFactory;
    }
    @Override
    public AbstractTask<Integer, Boolean, AsynchronousChannelRequestProperties> create(AsynchronousChannelRequestProperties properties,
                                                                                       Consumer<Integer> onProgress,
                                                                                       Consumer<Boolean> onComplete,
                                                                                       Consumer<Throwable> onFail) {

        return new DownloadFileTask(new FileStreamFileWriterFactory(),path,timeout,bufferSize,
                onProgress,
                onComplete,
                onFail,
                taskExceptionFactory);
    }
}
