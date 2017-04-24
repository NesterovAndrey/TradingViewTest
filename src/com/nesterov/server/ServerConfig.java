package com.nesterov.server;

import com.nesterov.StringConstants;
import com.nesterov.core.environment.Environment;
import com.nesterov.core.executions.builder.ExecutionMap;
import com.nesterov.core.executions.builder.ExecutionMapImpl;
import com.nesterov.core.executions.chain.DefaultExecutionChainBuilder;
import com.nesterov.core.executions.chain.ExecutionChainBuilder;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.*;
import com.nesterov.core.json.JSONMapper;
import com.nesterov.core.request.ActionType;
import com.nesterov.server.listeners.AsyncChannelMessageDeserializerFactory;
import com.nesterov.server.listeners.ExecutorsConnectionListener;
import com.nesterov.server.listeners.MessageDeserializerFactory;
import com.nesterov.server.service.Service;
import com.nesterov.server.service.ServiceContainer;
import com.nesterov.server.service.ServiceContainerImpl;
import com.nesterov.server.service.StatistcsService;
import com.nesterov.server.statistics.StatisticsStorage;
import com.nesterov.server.statistics.StatisticsStorageImpl;
import com.nesterov.server.folder.StreamFolderList;
import com.nesterov.server.task.fileDataTask.FileRequestTaskFactory;
import com.nesterov.server.task.fileResponse.WriteFileTaskFactory;
import com.nesterov.server.task.filesList.FilesListTaskFactory;
import com.nesterov.server.task.sendResponse.SendResponseTaskFactory;
import com.nesterov.server.task.statistics.FileStatisticsFactory;
import com.nesterov.util.fileSystem.MappedFileBytesLoaderFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.nesterov.core.environment.Environment.getEnvironmentInstance;
import static com.nesterov.core.environment.Environment.getEnvironmentPropertie;

public class ServerConfig {
    private static StatisticsStorage<ActionType> statisticsStorage= new StatisticsStorageImpl();
    private static Logger logger;
    private static ExecutorService executorService=Executors.newCachedThreadPool();
    private static Environment environment;
    private static ServiceContainer serviceContainer=new ServiceContainerImpl(executorService());
    private static Service statisticsService;
    private static ServerSocketListener listener;
    private static MessageDeserializerFactory<Map> messageDeserializerFactory=new AsyncChannelMessageDeserializerFactory();
    private static ExecutorsConnectionListener executorsConnectionListener=new ExecutorsConnectionListener(messageDeserializerBuilder(),taskExecutor(),executorService());

    public static TaskExecutor taskExecutor()
    {
       return new TaskExecutorImpl(executionMap());
    }
    public static JSONMapper jsonMapper=new JSONMapper();
    private static TaskExceptionFactory<RequestProperties> defaultTaskExceptionFactory=new DefaultTaskExceptionFactory<>();
    private static TaskExceptionFactory<AsynchronousChannelRequestProperties> asynchronousChannelTaskExceptionFactory=new AsynchronousChannelExceptionFactory();
    public static ExecutionMap<ActionType,ExecutionChainBuilder> executionMap()
    {
        ExecutionMap<ActionType,ExecutionChainBuilder> executionMap=new ExecutionMapImpl<>();

        executionMap.map(ActionType.LIST, listExecutionChainBuilder());

        executionMap.map(ActionType.FILE, fileExecutionChainBuilder());
        return executionMap;

    }
    private static ExecutionChainBuilder<String,RequestProperties> listExecutionChainBuilder()
    {
        Integer timeout=Integer.parseInt(getEnvironmentPropertie(StringConstants.WAIT_TIMEOUT));
        String pathStr=getEnvironmentPropertie(StringConstants.FOLDER);
        Path path=Paths.get(pathStr);
        ExecutionChainBuilder<String,RequestProperties> chainBuilder=new DefaultExecutionChainBuilder<RequestProperties>(new AsynchronousChannelRequestProperties())
                .thenExecute(new FilesListTaskFactory(new StreamFolderList(path),defaultTaskExceptionFactory()),StringConstants.EXECTION_LIST)
                .thenExecute(new SendResponseTaskFactory(timeout,executorService(),defaultTaskExceptionFactory()),StringConstants.EXECTION_RESPONSE);
        chainBuilder.onChainFail((e)->logger().info(e.getMessage()));
        return chainBuilder;
    }
    private static ExecutionChainBuilder<String,RequestProperties> fileExecutionChainBuilder()
    {
        String pathStr=getEnvironmentPropertie(StringConstants.FOLDER);
        Path path=Paths.get(pathStr);
        ExecutionChainBuilder<String,RequestProperties> chainBuilder=new DefaultExecutionChainBuilder<>(new AsynchronousChannelRequestProperties())
                .thenExecute(new FileRequestTaskFactory(path,defaultTaskExceptionFactory()), StringConstants.EXECTION_FILE)
                .thenExecute(new WriteFileTaskFactory(new MappedFileBytesLoaderFactory(),Integer.parseInt(getEnvironmentPropertie( StringConstants.BUFFER)),
                        Integer.parseInt(getEnvironmentPropertie( StringConstants.BUFFER)),asynchronousChannelTaskExceptionFactory()), StringConstants.EXECTION_WRITE)
                .thenExecute(new FileStatisticsFactory(statisticsStorage(),defaultTaskExceptionFactory()), StringConstants.EXECTION_STAT);
        chainBuilder.onChainFail((e)->logger().info(e.getMessage()));
        return chainBuilder;
    }

    private static TaskExceptionFactory<RequestProperties> defaultTaskExceptionFactory()
    {
        return defaultTaskExceptionFactory=defaultTaskExceptionFactory==null?new DefaultTaskExceptionFactory<>():defaultTaskExceptionFactory;
    }
    private static TaskExceptionFactory<AsynchronousChannelRequestProperties> asynchronousChannelTaskExceptionFactory()
    {
        return asynchronousChannelTaskExceptionFactory=asynchronousChannelTaskExceptionFactory==null?new AsynchronousChannelExceptionFactory():asynchronousChannelTaskExceptionFactory;
    }

    private static StatisticsStorage<ActionType> statisticsStorage()
    {
        return statisticsStorage;
    }
    public static Logger logger()
    {
        return logger=logger==null?newLogger():logger;
    }
    public static ExecutorService executorService()
    {
        return executorService;
    }
    public static ServiceContainer serviceContainer()
    {
        return serviceContainer;
    }
    public static Service statisticsService()
    {
        return statisticsService=statisticsService==null?newStatisticsService():statisticsService;
    }
    private static Service newStatisticsService()
    {
        String path=getEnvironmentPropertie(StringConstants.STAT_FILE);
       return new StatistcsService(Paths.get(path).toFile()
                ,statisticsStorage(),Integer.parseInt(getEnvironmentPropertie(StringConstants.STAT_TIMEOUT)), TimeUnit.SECONDS);
    }
    public static ServerSocketListener serverSocketListener()
    {
        return listener=listener==null?newServerSocketListener():listener;
    }
    private static ServerSocketListener newServerSocketListener()
    {
        ServerSocketListener serverSocketListener=null;
        try {
            serverSocketListener=new ServerSocketListener(Integer.parseInt(getEnvironmentPropertie(StringConstants.PORT)),executorService());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocketListener;
    }
    public static ExecutorsConnectionListener executorsConnectionListener()
    {
        return executorsConnectionListener;
    }


    public static MessageDeserializerFactory<Map> messageDeserializerBuilder()
    {
        return messageDeserializerFactory;
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
            String path=getEnvironmentInstance().getPropertie(StringConstants.LOG_FILE);
            File file=Paths.get(path).toFile();
            fileHandler = new FileHandler(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        return  logger;
    }
}
