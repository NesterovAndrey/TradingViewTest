package com.nesterov.server.net;

import com.nesterov.server.ServerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.channels.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsynchronousChannelConnection implements Connection {
    private final int port;

    private final CompletionHandler<AsynchronousSocketChannel,  AsynchronousServerSocketChannel> completionHandler;
    private boolean running=false;
    private ServerSocket serverSocket;
    private AsynchronousChannelGroup group;
    public AsynchronousChannelConnection(int port, CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> completionHandler) throws IOException {
        this.port=port;
        this.completionHandler=completionHandler;
        this.group=AsynchronousChannelGroup.withThreadPool(ServerConfig.executorService());
    }

    @Override
    public void connect() throws IOException, InterruptedException {

        AsynchronousServerSocketChannel server =
                AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(this.port));
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        server.accept(server,this.completionHandler);
        group.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() throws InterruptedException {
        group.awaitTermination(1, TimeUnit.NANOSECONDS);
        group.shutdown();
    }

}
