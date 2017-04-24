package com.nesterov.client;

import com.nesterov.StringConstants;
import com.nesterov.client.presenter.MainPresenter;
import com.nesterov.client.presenter.MainPresenterImpl;
import com.nesterov.client.view.ViewImpl;
import com.nesterov.core.executions.task.TaskExecutor;
import com.nesterov.server.ServerConfig;
import sun.tools.jar.CommandLine;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.nesterov.core.environment.Environment.fillEnvironment;

public class ClientMain {

    static MainPresenter presenter;

    public static void main(String[] args) throws ClassNotFoundException, IOException {

            //Заполняем параметры из аргументов командной строки
            List<String> argList = Arrays.asList(CommandLine.parse(args));
            fillEnvironment(StringConstants.IP, "-ip", "127.0.0.1", argList);
            fillEnvironment(StringConstants.PORT, "-p", "9999", argList);
            fillEnvironment(StringConstants.BUFFER, "-b", "1000", argList);
            fillEnvironment(StringConstants.FOLDER, "-f", "downloads", argList);
            fillEnvironment(StringConstants.LOG_FILE, "-l", "clientLog.log", argList);
            fillEnvironment(StringConstants.WAIT_TIMEOUT, "-wt", "10", argList);
            ExecutorService executorService = ClientConfig.executorService();

            TaskExecutor taskExecutor = ClientConfig.taskExecutor();

            FileTransferClient client=new FileTransferClient(taskExecutor);

            presenter = new MainPresenterImpl(client, new ViewImpl(System.out,System.in),ClientConfig.executorService());
            ClientConfig.executorService().shutdown();
    }

}
