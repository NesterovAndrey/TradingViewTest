package com.nesterov.client.task.connection;


import com.nesterov.client.connection.Connection;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Optional;
import java.util.function.Consumer;

public class CreateConnectionTaskFactory implements TaskFactory<Float,Boolean, AsynchronousChannelRequestProperties> {
    private final Connection<AsynchronousSocketChannel> connection;
    private final InetSocketAddress address;
    private final TaskExceptionFactory<AsynchronousChannelRequestProperties> taskExceptionFactory;
    public CreateConnectionTaskFactory(Connection<AsynchronousSocketChannel> connection,
                                       InetSocketAddress address,
                                       TaskExceptionFactory<AsynchronousChannelRequestProperties> taskExceptionFactory)
    {
        this.connection= Optional.of(connection).get();
        this.address=Optional.of(address).get();
        this.taskExceptionFactory=Optional.of(taskExceptionFactory).get();
    }
    @Override
    public AbstractTask<Float, Boolean,  AsynchronousChannelRequestProperties> create(AsynchronousChannelRequestProperties properties,
                                                                                      Consumer<Float> onProgress,
                                                                                      Consumer<Boolean> onComplete,
                                                                                      Consumer<Throwable> onFail) {
        return new CreateConnectionTask(connection,address,onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
