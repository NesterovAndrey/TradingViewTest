package com.nesterov.server.task.fileDataTask;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.server.ServerConfig;
import java.io.File;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Path;
import java.util.function.Consumer;

//Обрабатывает запрос файла
public class FileRequestTask extends AbstractTask<Float, Void, RequestProperties> {

    private final Path folder;
    private AsynchronousSocketChannel channel;

    protected FileRequestTask(Path folder,
                           Consumer<Float> onProgress,
                           Consumer<Void> onComplete,
                           Consumer<Throwable> onFail,
                           TaskExceptionFactory<RequestProperties> exceptionFactory) {
        super(onProgress, onComplete, onFail,exceptionFactory);
        this.folder = folder;

    }
    //Проверяет существует ли запрошенный файл
    @Override
    public Void execute(ExecutionChain<RequestProperties> chain, RequestProperties requestProperties)  throws Exception {
            File file = new File(String.format("%s/%s", this.folder, requestProperties.get(StringConstants.DOWNLOAD_FILE_NAME)));
            ServerConfig.logger().info(String.format("Checking file:%s", file.getName()));
            checkFile(file);
            requestProperties.put(StringConstants.FILE,file);
        return null;
    }
    private void checkFile(File file) throws RuntimeException
    {
        if (!file.exists()) {
            throw new RuntimeException("FILE NOT EXISTS");
        }
    }


}
