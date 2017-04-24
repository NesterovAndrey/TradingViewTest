package com.nesterov.client.task.sendRequest;

import com.nesterov.StringConstants;
import com.nesterov.client.ClientConfig;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.json.JsonBuilder;

import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Отправляет запрос на сервер
 */
public class SendRequestTask extends AbstractTask<Float,Boolean,RequestProperties> {
    private final int timeout;
    private final ExecutorService executorService;
    protected SendRequestTask(int timeout,ExecutorService executorService,
                           Consumer<Float> onProgress,
                           Consumer<Boolean> onComplete,
                           Consumer<Throwable> onFail,
                           TaskExceptionFactory<RequestProperties> exceptionFactory)
    {
        super(onProgress,onComplete,onFail,exceptionFactory);
        this.timeout = timeout;
        this.executorService=executorService;
    }

    @Override
    public Boolean execute(ExecutionChain<RequestProperties> chain, RequestProperties requestProperties) throws Exception {
        ClientConfig.logger().info(String.format("Sending request to server"));
        sendData(requestProperties.getOutputStream(),buildData(requestProperties));
        return true;
    }
    private String buildData(RequestProperties requestProperties)
    {
        JsonBuilder jsonBuilder=new JsonBuilder();
        requestProperties.keys().forEach((s)->
        {
            String string=requestProperties.get(s).toString();
            jsonBuilder.append(s,string);
        });
        return jsonBuilder.build();
    }
    private void sendData(OutputStream outputStream,String data) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        FutureTask<Boolean> future=new FutureTask<>(()->{
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            return true;
        });
        this.executorService.submit(future);
        future.get(this.timeout, TimeUnit.SECONDS);

    }
}
