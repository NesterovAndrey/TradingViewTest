package com.nesterov.client.task.downloadFile;

import com.nesterov.client.ClientConfig;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.util.fileSystem.BytesWriter;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

public class DownloadCompletionHandler implements CompletionHandler<Integer, AbstractTask<Integer, Boolean, AsynchronousChannelRequestProperties>> {

    private final AsynchronousSocketChannel channel;
    private final File file;
    private final ByteBuffer buffer;
    private final Consumer<Throwable> throwException;
    private final Consumer<Integer> onProgress;
    private final BytesWriter bytesWriter;
    public DownloadCompletionHandler(AsynchronousSocketChannel channel,
                                     File file,
                                     ByteBuffer buffer,
                                     BytesWriter bytesWriter,
                                     Consumer<Throwable> throwException,
                                     Consumer<Integer> onProgress)
    {
        this.channel=channel;
        this.file=file;
        this.buffer=buffer;
        this.bytesWriter=bytesWriter;
        this.throwException=throwException;
        this.onProgress=onProgress;
    }
    @Override
    public void completed(Integer result, AbstractTask<Integer, Boolean,  AsynchronousChannelRequestProperties> attachment) {
        try {
            if (result > 0) {
                this.onProgress.accept(result);
                loadNextBytes(file, buffer);
                attachment.updateProgress(result);
                channel.read(buffer, attachment, this);
            }else releaseMonitor();
              } catch (Exception e) {
            failed(e,attachment);
        }
    }

    @Override
    public void failed(Throwable exc, AbstractTask<Integer, Boolean, AsynchronousChannelRequestProperties> attachment) {
        throwException.accept(exc);
        releaseMonitor();
    }

    private void loadNextBytes(File file, ByteBuffer buffer) {
        ClientConfig.logger().info(String.format("Loading file data file:%s", file.getName()));
        bytesWriter.writeNext(buffer.array());
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
