package com.nesterov.server;

import com.nesterov.server.net.Connection;
import com.nesterov.server.net.AsynchronousChannelConnection;
import com.nesterov.util.observer.Observable;
import com.nesterov.util.observer.ObservableWrapperImpl;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
//Ожидает подклёчения к серверу и вызывает подписанные классы
public final class ServerSocketListener {
    private final Observable observable=new ObservableWrapperImpl();
    private final Connection connection;
    private final ExecutorService executorService;

    public ServerSocketListener(int port, ExecutorService executorService) throws IOException {
        this.executorService=executorService;

        connection = new AsynchronousChannelConnection(port,
                new CompletionHandler<AsynchronousSocketChannel,
                        AsynchronousServerSocketChannel>() {
            @Override
            public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                observable.notifyListeners(result);
                attachment.accept(attachment, this);
            }
            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

            }
        });
    }

    public void start() throws IOException, InterruptedException {
        ServerConfig.logger().info("STARTING SERVER");
        connection.connect();
        ServerConfig.logger().info("SERVER STARTED");

    }
    public void stop() throws InterruptedException {
        connection.shutdown();
    }
    public ServerSocketListener addListener(Observer listener) {
        this.observable.addListener(listener);
        return this;
    }
}
