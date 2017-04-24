package com.nesterov.server.task.fileResponse;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.server.ServerConfig;
import com.nesterov.util.fileSystem.BytesLoader;
import com.nesterov.util.fileSystem.BytesLoaderFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Записывает файл в поток по частям
 */
public class WriteFileTask extends AbstractTask<Integer, Void, AsynchronousChannelRequestProperties> {
    private final BytesLoaderFactory bytesLoaderFactory;
    private final int bufferSize;
    private final int timeout;
    private BytesLoader bytesLoader;

    protected WriteFileTask(BytesLoaderFactory bytesLoaderFactory,
                         int bufferSize,
                         int timeout,
                         Consumer<Integer> onProgress,
                         Consumer<Void> onComplete,
                         Consumer<Throwable> onFail,
                         TaskExceptionFactory<AsynchronousChannelRequestProperties> exceptionFactory) {

        super(onProgress,onComplete,onFail,exceptionFactory);
        this.bytesLoaderFactory = bytesLoaderFactory;
        this.bufferSize = bufferSize;
        this.timeout=timeout;
    }


    @Override
    protected Void execute(ExecutionChain<AsynchronousChannelRequestProperties> chain, AsynchronousChannelRequestProperties requestProperties) throws Exception {
        File file = (File) requestProperties.get(StringConstants.FILE);
            ServerConfig.logger().info(String.format("Trying to send file %s to:%s", file.getName(), requestProperties.get(StringConstants.IP)));

            this.bytesLoader = this.bytesLoaderFactory.create(file);

            CompletionHandler<Integer, AbstractTask<Integer, Void, AsynchronousChannelRequestProperties>> handler=
                    new WriteDataCompletionHandler(bytesLoader,bufferSize,
                            requestProperties.getSocketChannel(),
                            requestProperties.getSocketChannel().getRemoteAddress(),
                            file,(e)->callOnFail(e,requestProperties),(result)->updateProgress(result));

            synchronized (handler) {
                requestProperties.getSocketChannel().write(ByteBuffer.wrap(bytesLoader.loadNext(bufferSize)), timeout, TimeUnit.SECONDS, this, handler);
                handler.wait();
            }

            requestProperties.getSocketChannel().shutdownOutput();
            ServerConfig.logger().info(String.format("File %s loading to:%s completed", file.getName(), requestProperties.get(StringConstants.IP)));
        return null;
    }

}
