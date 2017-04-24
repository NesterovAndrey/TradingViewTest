package com.nesterov.client.task.filesList;

import com.nesterov.client.connection.Connection;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.DefaultTaskExceptionFactory;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class FileListTaskFactory implements TaskFactory<Float,String, RequestProperties> {
    private final int timeout;
    private final ExecutorService executorService;
    private final Connection<AsynchronousSocketChannel> connection;
    private final InetSocketAddress address;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public FileListTaskFactory(int timeout,ExecutorService executorService,Connection<AsynchronousSocketChannel> connection,
                               InetSocketAddress address,
                               TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.timeout=timeout;
        this.executorService=executorService;
        this.connection=connection;
        this.address=address;
        this.taskExceptionFactory=taskExceptionFactory;
    }
    @Override
    public AbstractTask<Float,String, RequestProperties> create(RequestProperties properties,
                                                                Consumer<Float> onProgress,
                                                                Consumer<String> onComplete,
                                                                Consumer<Throwable> onFail) {
        return new FilesListTask(this.timeout,this.executorService,onProgress, onComplete,onFail,new DefaultTaskExceptionFactory<>());
    }
}
