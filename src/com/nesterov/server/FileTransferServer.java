package com.nesterov.server;
import com.nesterov.server.service.ServiceContainer;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class FileTransferServer {
    private final ServiceContainer serviceContainer;
    private final ServerSocketListener listener;

    private final ExecutorService executionService;

    public FileTransferServer(ServiceContainer serviceContainer,ServerSocketListener listener,ExecutorService executionService)
    {
        this.serviceContainer=serviceContainer;
        this.listener=listener;
        this.executionService=executionService;
    }
    public void start() throws IOException, InterruptedException {
        this.listener.start();
    }
    public void stop() throws InterruptedException {
        this.executionService.shutdown();
        this.listener.stop();
        this.serviceContainer.stop();

    }
    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }

    public ServerSocketListener getListener() {
        return listener;
    }


}
