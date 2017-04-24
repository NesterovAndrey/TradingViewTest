package com.nesterov.server;

import com.nesterov.StringConstants;
import com.nesterov.server.service.ServiceContainer;
import sun.tools.jar.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static com.nesterov.core.environment.Environment.fillEnvironment;

public class ServerMain {

    static FileTransferServer server;
    public static void main(String[] args) {
        try {
            //Заполняем переменные из аргументов коммандой строки
        List<String> argList= Arrays.asList(CommandLine.parse(args));
            fillEnvironment(StringConstants.FOLDER,"-f","files",argList);
            fillEnvironment(StringConstants.PORT,"-p","9999",argList);
            fillEnvironment(StringConstants.STAT_FILE,"-s","serverStat.txt",argList);
            fillEnvironment(StringConstants.LOG_FILE,"-l","serverLog.log",argList);
            fillEnvironment(StringConstants.STAT_TIMEOUT,"-st","10",argList);
            fillEnvironment(StringConstants.BUFFER,"-b","1000",argList);
            fillEnvironment(StringConstants.WAIT_TIMEOUT, "-wt", "10", argList);
            ServiceContainer serviceContainer=ServerConfig.serviceContainer();
            serviceContainer.addService(ServerConfig.statisticsService());
            ServerSocketListener serverSocketListener = ServerConfig.serverSocketListener();
            serverSocketListener.addListener(ServerConfig.executorsConnectionListener());
            server=new FileTransferServer(serviceContainer,serverSocketListener,ServerConfig.executorService());
            server.start();
            ServerConfig.executorService().submit(()->waitToStop(serverSocketListener,serviceContainer));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
        //ожидает ввода команды для завершеня работы
        private static void waitToStop(ServerSocketListener serverSocketListener,ServiceContainer serviceContainer)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String result=br.readLine();
                if(result.equals("stop"))
                {
                    server.stop();
                }
                else waitToStop(serverSocketListener,serviceContainer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

}
