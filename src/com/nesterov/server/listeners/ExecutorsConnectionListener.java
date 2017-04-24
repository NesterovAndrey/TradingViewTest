package com.nesterov.server.listeners;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChainBuilder;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.task.TaskExecutor;
import com.nesterov.core.request.ActionType;
import com.nesterov.server.ServerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class ExecutorsConnectionListener implements Observer {
    private final MessageDeserializerFactory<Map> messageDeserializerFactory;
    private final TaskExecutor<ActionType> taskExecutor;
    private final ExecutorService executorService;
    public ExecutorsConnectionListener(MessageDeserializerFactory<Map> messageDeserializerFactory, TaskExecutor taskExecutor, ExecutorService executorService) {
        this.messageDeserializerFactory = Optional.of(messageDeserializerFactory).get();
        this.taskExecutor = Optional.of(taskExecutor).get();
        this.executorService=Optional.of(executorService).get();
    }
    //Вызывается при новом подключении к серверу.
    @Override
    public void update(Observable o, Object arg) {
        executorService.submit(()->
        {
        try {
            AsynchronousSocketChannel asynchronousSocketChannel = (AsynchronousSocketChannel) arg;
            OutputStream outputStream = Channels.newOutputStream(asynchronousSocketChannel);
            InputStream inputStream = Channels.newInputStream(asynchronousSocketChannel);
            //Преобразует запрос
            Map request = messageDeserializerFactory.create(inputStream).deserialize();

            request.put(StringConstants.IP,asynchronousSocketChannel.getRemoteAddress().toString());

            ServerConfig.logger().info(String.format("Request from %s\n data:%s", request.get(StringConstants.IP),request));

            //Создаёт первоначальные параметры для цепочки вызовов задач
            AsynchronousChannelRequestProperties channelProperties = new  AsynchronousChannelRequestProperties(request);
            channelProperties.setSocketChannel(asynchronousSocketChannel);

            //Создаём и вызываем новую цепочку вызовув задач
            ActionType actionType = ActionType.valueOf((String) request.get(StringConstants.TYPE));
            taskExecutor.execution(actionType).<String,  AsynchronousChannelRequestProperties>chainProperties(channelProperties);
            ExecutionChainBuilder<String,  AsynchronousChannelRequestProperties> defaultExecutionChainBuilder = taskExecutor.execution(actionType);

            defaultExecutionChainBuilder.build().doNext(channelProperties);
        } catch (IOException e) {
           ServerConfig.logger().info(e.toString());
        }
        });
    }
}
