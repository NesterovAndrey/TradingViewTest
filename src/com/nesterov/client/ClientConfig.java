package com.nesterov.client;

import com.nesterov.StringConstants;
import com.nesterov.client.connection.AsynchronousChannelConnection;
import com.nesterov.client.connection.Connection;
import com.nesterov.client.task.connection.CreateConnectionTaskFactory;
import com.nesterov.client.task.downloadFile.DownloadFileTaskFactory;
import com.nesterov.client.task.filesList.FileListTaskFactory;
import com.nesterov.client.task.requestType.RequestTypeTaskFactory;
import com.nesterov.client.task.sendRequest.SendRequestTaskFactory;
import com.nesterov.core.executions.builder.ExecutionMap;
import com.nesterov.core.executions.builder.ExecutionMapImpl;
import com.nesterov.core.executions.chain.DefaultExecutionChainBuilder;
import com.nesterov.core.executions.chain.ExecutionChainBuilder;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.*;
import com.nesterov.core.json.JSONMapper;
import com.nesterov.core.request.ActionType;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.nesterov.core.environment.Environment.getEnvironmentInstance;
import static com.nesterov.core.environment.Environment.getEnvironmentPropertie;

public class ClientConfig {

    private static Logger logger;
    private static TaskExecutor taskExecutor;
    private static Connection<AsynchronousSocketChannel> connection;
    private static AsynchronousChannelGroup group;
    private static ExecutorService executorService=Executors.newSingleThreadExecutor();
    private static InetSocketAddress address;
    private static JSONMapper jsonMapper=new JSONMapper();
    private static TaskExceptionFactory<RequestProperties> defaultTaskExceptionFactory=new DefaultTaskExceptionFactory<>();
    private static TaskExceptionFactory<AsynchronousChannelRequestProperties> asynchronousChannelTaskExceptionFactory=new AsynchronousChannelExceptionFactory();
    public static TaskExecutor taskExecutor(int buffer, Connection<AsynchronousSocketChannel> connection, InetSocketAddress hostAddress)
    {
        return  new TaskExecutorImpl(executionMap(buffer,connection,hostAddress));
    }
    private static ExecutionMap<ActionType,ExecutionChainBuilder> executionMap(int buffer, Connection<AsynchronousSocketChannel> connection, InetSocketAddress hostAddress)
    {
        ExecutionMap executionMap = new ExecutionMapImpl();
        executionMap.map(ActionType.LIST, listExecutionBuilderItem(buffer,connection,hostAddress));
        executionMap.map(ActionType.FILE, fileExecutionBuilder(buffer,connection,hostAddress));
        return executionMap;

    }
    //Создаём маппинг билдера цепочки вызова команд для получения списка файлов
    //Кождый билдер вызова может быть получен по ключу передаваемому во втором параметре метода thenExecute
    //Для содания цепочки вызова задач надо
    //- Создать ExecutionChainBuilder
    //- Замапить билдеры конкретных задач с помщью вызова thenExecute
    //Каждый билер можно получить по ключу передаваемому в параметре executionBuilderName
    // (В thenExecute можно сразу передавать TaskFactory, а ExecutionBuidler будет создан автоматчески конкретной реализацей ExecutionChainBuilder
    //- Вызвать build у объекта ExecutionChainBuilder(Есть воможность добавть колбеки для успешного завершеня(onChainComplete)
    // и для обработки исключения onFail
    //- Вызвать run у объекта ExecutionChain
    private static ExecutionChainBuilder listExecutionBuilderItem(int bufferSize, Connection<AsynchronousSocketChannel> connection, InetSocketAddress hostAddress)
    {
        Integer timeout=Integer.parseInt(getEnvironmentPropertie(StringConstants.WAIT_TIMEOUT));
        return new DefaultExecutionChainBuilder<>(new  AsynchronousChannelRequestProperties())
                        .thenExecute(new CreateConnectionTaskFactory(connection,hostAddress,asynchronousChannelTaskExceptionFactory()), StringConstants.EXECTION_CONNECTION)
                        .thenExecute(new RequestTypeTaskFactory(ActionType.LIST,defaultTaskExceptionFactory()),  StringConstants.EXECTION_TYPE)
                        .thenExecute(new SendRequestTaskFactory(timeout,executorService(),defaultTaskExceptionFactory()), StringConstants.EXECTION_REQUEST)
                        .thenExecute(new FileListTaskFactory(timeout,executorService(),connection, hostAddress,defaultTaskExceptionFactory()),  StringConstants.EXECTION_LIST);
    }
    //Создаём маппинг билдера цепочки вызова команд для получения файла
    //Кождый билдер вызова может быть получен по ключу передаваемому во втором параметре метода thenExecute
    private static ExecutionChainBuilder fileExecutionBuilder(int bufferSize, Connection<AsynchronousSocketChannel> connection, InetSocketAddress hostAddress)
    {
        String path=getEnvironmentPropertie(StringConstants.FOLDER);
        Integer timeout=Integer.parseInt(getEnvironmentPropertie(StringConstants.WAIT_TIMEOUT));
        return new DefaultExecutionChainBuilder<>(new AsynchronousChannelRequestProperties())
                        .thenExecute(new CreateConnectionTaskFactory(connection,hostAddress,
                                asynchronousChannelTaskExceptionFactory()),StringConstants.EXECTION_CONNECTION)
                        .thenExecute(new RequestTypeTaskFactory(ActionType.FILE,defaultTaskExceptionFactory()),StringConstants.EXECTION_TYPE)
                        .thenExecute(new SendRequestTaskFactory(timeout,executorService(),defaultTaskExceptionFactory()),StringConstants.EXECTION_REQUEST)
                        .thenExecute(new DownloadFileTaskFactory(Paths.get(path),timeout,bufferSize,
                                asynchronousChannelTaskExceptionFactory()), StringConstants.EXECTION_FILE);
    }
    private static TaskExceptionFactory<RequestProperties> defaultTaskExceptionFactory()
    {
        return defaultTaskExceptionFactory;
    }
    private static TaskExceptionFactory<AsynchronousChannelRequestProperties> asynchronousChannelTaskExceptionFactory()
    {
        return asynchronousChannelTaskExceptionFactory;
    }
    private static TaskExecutor newTaskExecutor()
    {
        return new TaskExecutorImpl(executionMap(Integer.parseInt(getEnvironmentPropertie(StringConstants.BUFFER)),connection(),address()));
    }
    public static TaskExecutor taskExecutor()
    {
        return taskExecutor=taskExecutor==null?newTaskExecutor():taskExecutor;
    }
    private static Connection<AsynchronousSocketChannel> newConnection()
    {
        Connection<AsynchronousSocketChannel> result=null;
        try {
           result= new AsynchronousChannelConnection(group());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static Connection<AsynchronousSocketChannel> connection()
    {
        return connection=connection==null?newConnection():connection;
    }
    private static AsynchronousChannelGroup newGroup()
    {
        AsynchronousChannelGroup result=null;
        try {
            result=AsynchronousChannelGroup.withThreadPool(executorService());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static AsynchronousChannelGroup group()
    {
        return group=group==null?newGroup():group;
    }
    public static ExecutorService executorService() {
        return executorService;
    }
    public static InetSocketAddress address()
    {
        return address=address==null?newAddress():address;
    }
    private static InetSocketAddress newAddress()
    {
        InetSocketAddress result=null;
        try {
            result=new InetSocketAddress(InetAddress.getByName(getEnvironmentPropertie(StringConstants.IP)),
                    Integer.parseInt(getEnvironmentInstance().getPropertie(StringConstants.PORT)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JSONMapper jsonMapper()
    {
        return jsonMapper;
    }
    private static Logger newLogger()
    {
        Logger logger=Logger.getLogger("Logger");
        FileHandler fileHandler = null;
        try {
            String path=getEnvironmentPropertie(StringConstants.LOG_FILE);
            File file= Paths.get(path).toFile();
            fileHandler = new FileHandler(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        return  logger;
    }
    public static Logger logger()
    {
        return logger=logger==null?newLogger():logger;
    }
}
