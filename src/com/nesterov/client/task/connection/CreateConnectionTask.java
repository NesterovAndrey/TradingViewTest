package com.nesterov.client.task.connection;

import com.nesterov.client.ClientConfig;
import com.nesterov.client.connection.Connection;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Пытается подключить к серверу
 */
public class CreateConnectionTask extends AbstractTask<Float,Boolean, AsynchronousChannelRequestProperties> {
    private final Connection<AsynchronousSocketChannel> connection;
    private final InetSocketAddress address;
    protected CreateConnectionTask(Connection<AsynchronousSocketChannel> connection, InetSocketAddress address,
                                Consumer<Float> onProgress,
                                Consumer<Boolean> onComplete,
                                Consumer<Throwable> onFail,
                                TaskExceptionFactory<AsynchronousChannelRequestProperties> exceptionFactory)
    {
        super(onProgress,onComplete,onFail,exceptionFactory);
        this.connection=connection;
        this.address=address;
    }
    @Override
    public Boolean execute(ExecutionChain< AsynchronousChannelRequestProperties> chain, AsynchronousChannelRequestProperties requestProperties) throws Exception{
        ClientConfig.logger().info(String.format("Connecting to %s",address));
        AsynchronousSocketChannel channel= null;
        channel=tryConnect(address);
        requestProperties.setSocketChannel(channel);
        return true;
    }
    private AsynchronousSocketChannel tryConnect(InetSocketAddress address) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        AsynchronousSocketChannel channel = this.connection.connect(address);
        return channel;
    }
}
