package com.nesterov.client.task.filesList;

import com.nesterov.client.ClientConfig;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;

import java.io.*;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Читает ответ от сервера и возвращает результат в onComplete
 */

public class FilesListTask extends AbstractTask<Float,String, RequestProperties> {


    private AsynchronousSocketChannel channel;
    private final int timeout;
    private final ExecutorService executorService;

    protected FilesListTask(int timeout,ExecutorService executorService,
                         Consumer<Float> onProgress,
                         Consumer<String> onComplete,
                         Consumer<Throwable> onFail,
                         TaskExceptionFactory<RequestProperties> exceptionFactory) {
        super(onProgress, onComplete,onFail,exceptionFactory);
       this.timeout=timeout;
       this.executorService= Optional.of(executorService).get();
    }

    @Override
    public String execute(ExecutionChain<RequestProperties> executionChain, RequestProperties requestProperties) throws Exception {
        ClientConfig.logger().info(String.format("Retrieving list of files"));
        return readData(requestProperties.getInputStream());
    }
    private String readData(InputStream inputStream) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String result=null;
            FutureTask<String> future=new FutureTask<String>(()
            ->{
                ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
                return (String)objectInputStream.readObject();
            });
            executorService.submit(future);
            result=future.get(timeout, TimeUnit.SECONDS);
            inputStream.close();
        return result;
    }
}
