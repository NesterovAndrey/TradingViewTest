package com.nesterov.client.task.downloadFile;

import com.nesterov.StringConstants;
import com.nesterov.client.ClientConfig;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.util.fileSystem.BytesWriter;
import com.nesterov.util.fileSystem.BytesWriterFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Получает байты из файла на сервере и сохраняет на диск
 */
public class DownloadFileTask extends AbstractTask<Integer, Boolean, AsynchronousChannelRequestProperties> {
    private final BytesWriterFactory bytesWriterFactory;
    private final int timeout;
    private final int bufferSize;
    private final Path path;
    private BytesWriter bytesWriter;
    private AsynchronousSocketChannel channel;
    protected DownloadFileTask(BytesWriterFactory bytesWriterFactory,
                            Path path,int timeOut,int bufferSize,
                            Consumer<Integer> onProgress,
                            Consumer<Boolean> onComplete,
                            Consumer<Throwable> onFail,
                            TaskExceptionFactory<AsynchronousChannelRequestProperties> exceptionFactory) {
        super(onProgress, onComplete, onFail,exceptionFactory);
        this.bytesWriterFactory = bytesWriterFactory;
        this.path = path;
        this.timeout=timeOut;
        this.bufferSize=bufferSize;
    }

    @Override
    public Boolean execute(ExecutionChain<AsynchronousChannelRequestProperties> executionChain, AsynchronousChannelRequestProperties requestProperties) throws Exception {
        File file = new File(String.format("%s/%s", this.path,
                requestProperties.get(StringConstants.DOWNLOAD_FILE_NAME)));

        ClientConfig.logger().info(String.format("Requesting a file:%s", file.getName()));
        channel = requestProperties.getSocketChannel();
        this.bytesWriter=this.bytesWriterFactory.create(file);
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        ClientConfig.logger().info(String.format("Loading file:%s", file.getName()));

        CompletionHandler<Integer, AbstractTask<Integer, Boolean, AsynchronousChannelRequestProperties>> handler=
            new DownloadCompletionHandler(channel,file,buffer,bytesWriter,(e)->throwException(e),(bytesCount)->updateProgress(bytesCount));

        synchronized (handler) {
            channel.read(buffer, timeout, TimeUnit.SECONDS, this, handler);
            handler.wait();
        }
        shutdown();

        return true;
    }
    private void loadData(ByteBuffer buffer,File file) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Integer> loadFuture=null;
        while ((loadFuture=channel.read(buffer)).get(timeout,TimeUnit.SECONDS)>0)
        {
            bytesWriter.writeNext(buffer.array());
            ClientConfig.logger().info(String.format("Loaded :%s bytes",buffer.array().length));
            buffer.clear();
        }
    }

    private void shutdown()
    {
       bytesWriter.close();
        try {
            channel.shutdownInput();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void throwException(Throwable e) {
       throw new RuntimeException(e.getMessage());
    }

}
