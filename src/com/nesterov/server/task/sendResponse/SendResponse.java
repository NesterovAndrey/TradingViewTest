package com.nesterov.server.task.sendResponse;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.server.ServerConfig;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Отправляет ответ клиенту серверу
 */
public class SendResponse extends AbstractTask<Float,Void,RequestProperties> {

    private final int timeout;
    private final ExecutorService executorService;
    protected SendResponse(int timeout,ExecutorService executorService,
                        Consumer<Float> onProgress,
                        Consumer<Void> onComplete,
                        Consumer<Throwable> onFail,
                        TaskExceptionFactory<RequestProperties> exceptionFactory) {
        super(onProgress, onComplete, onFail,exceptionFactory);
        this.timeout=timeout;
        this.executorService=executorService;
    }

    @Override
    protected Void execute(ExecutionChain<RequestProperties> chain, RequestProperties requestProperties) throws Exception {
        ServerConfig.logger().info(String.format("Start sending data to %s",requestProperties.get(StringConstants.IP)));
        writeData(requestProperties.getOutputStream(),(String)requestProperties.get(StringConstants.RESPONSE_DATA));

        ServerConfig.logger().info(String.format("Data was sent to %s",requestProperties.get(StringConstants.IP)));
        return null;
    }
    private void writeData(OutputStream outputStream,String data) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask<Boolean> future=new FutureTask<Boolean>(()->
        {
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            return true;
        });
        executorService.submit(future);
        future.get(this.timeout, TimeUnit.SECONDS);

    }
}
