package com.nesterov.server.task.fileResponse;

import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.server.ServerConfig;
import com.nesterov.util.fileSystem.BytesLoader;

import java.io.File;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

public class WriteDataCompletionHandler implements CompletionHandler<Integer, AbstractTask<Integer, Void, AsynchronousChannelRequestProperties>> {
    private final BytesLoader bytesLoader;
    private final int bufferSize;
    private final AsynchronousSocketChannel channel;
    private final SocketAddress socketAddress;
    private final File file;
    private final Consumer<Throwable> throwException;
    private final Consumer<Integer> onProgress;
    public WriteDataCompletionHandler(BytesLoader bytesLoader,
                                      int bufferSize,
                                      AsynchronousSocketChannel channel,
                                      SocketAddress address,
                                      File file,
                                      Consumer<Throwable>throwException,
                                      Consumer<Integer> onProgress)
    {
        this.bytesLoader=bytesLoader;
        this.bufferSize=bufferSize;
        this.channel=channel;
        this.socketAddress=address;
        this.file=file;
        this.throwException=throwException;
        this.onProgress=onProgress;
    }
    @Override
    public void completed(Integer result, AbstractTask<Integer, Void, AsynchronousChannelRequestProperties> attachment) {
       if (result>0) {
           //writeData(bytesLoader, channel);
           ServerConfig.logger().info(String.format("Bytes %s sent to:%s", result, socketAddress));
           this.onProgress.accept(result);
           channel.write(ByteBuffer.wrap(bytesLoader.loadNext(bufferSize)),attachment,this);
       }
       else releaseMonitor();

    }

    @Override
    public void failed(Throwable exc, AbstractTask<Integer, Void, AsynchronousChannelRequestProperties> attachment) {
        throwException.accept(exc);
    }
    private void writeData(BytesLoader bytesLoader, AsynchronousSocketChannel channel){
            ByteBuffer buffer = ByteBuffer.wrap(this.bytesLoader.loadNext(bufferSize));

            buffer.clear();
    }
    private void releaseMonitor()
    {
        synchronized(this)
        {
            this.notify();
        }
    }
}
