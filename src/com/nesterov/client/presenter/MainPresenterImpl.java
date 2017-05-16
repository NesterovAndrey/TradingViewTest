package com.nesterov.client.presenter;

import com.nesterov.StringConstants;
import com.nesterov.client.ClientConfig;
import com.nesterov.client.FileTransferClient;
import com.nesterov.client.presenter.input.ActionData;
import com.nesterov.client.presenter.input.ActionDataImpl;
import com.nesterov.client.presenter.input.InputParser;
import com.nesterov.client.view.ViewImpl;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.request.ActionType;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

//Класс для управления отображением
public class MainPresenterImpl implements MainPresenter {



    private Map<Commands,ActionData<String, AsynchronousChannelRequestProperties,String[]>> actions=new HashMap<>();
    private FileTransferClient<ActionType> client;
    private ViewImpl viewImpl;
    private ExecutorService executorService;
    public MainPresenterImpl(FileTransferClient<ActionType> client, ViewImpl viewImpl,ExecutorService executorService)
    {
        this.client=client;
        this.viewImpl = viewImpl;
        this.executorService=executorService;
        //Маппинги команд из коммандной строки и команд из цепочек выполнения задач
        actions.put(Commands.LIST,new ActionDataImpl(Commands.LIST,"Retrievs a list of files",new String[0],(properties)->doList(properties)));
        actions.put(Commands.FILE,new ActionDataImpl(Commands.FILE,"Download file from the server",new String[]{StringConstants.FILENAME},(properties)->doFile(properties)));
        actions.put(Commands.QUIT,new ActionDataImpl(Commands.QUIT,"Close program.",new String[0],(properties)->{
            System.out.println("Closing program");
        }));

        showCommands();
    }
    //Создаёт цепочку выполнения задач для запроса списка
    private void doList(AsynchronousChannelRequestProperties chainProperties) {

        ActionType actionType = ActionType.LIST;
        this.client.getTaskExecutor()
                .execution(actionType)
                .onChainFail((e)->
                {
                    viewImpl.showMessage(String.format("Cant get files list\n%s",e.getMessage()));
                    showCommands();
                })
                .onChainComplete((ob)->showCommands())
                .<Float,String,Properties>onComplete(StringConstants.EXECTION_LIST,(String result)->
                {
                    Map<Objects,List> resultMap= ClientConfig.jsonMapper().map(result);
                    this.viewImpl.showFiles(resultMap.get("filename"));
                });
        this.client.getTaskExecutor().
                execution(actionType).build().doNext(chainProperties);

    }
    //Создаёт цепочку выполнения задач для запроса файла
    private void doFile(AsynchronousChannelRequestProperties chainProperties)
    {
        ActionType actionType = ActionType.FILE;
        this.client.getTaskExecutor()
                .execution(actionType)
                .onChainFail((e)->
                {
                    viewImpl.showMessage(String.format("Cant download file\n%s",e.getMessage()));
                    showCommands();
                })
                .onChainComplete((ob)->showCommands())
                .<Integer,Boolean,Properties>onProgress(StringConstants.FILE,
                        (Integer progress)->viewImpl.showMessage(String.format("Bytes loaded:%d",progress)));

        this.client.getTaskExecutor()
                .execution(actionType).build().doNext(chainProperties);
    }

    //Вызыв команды
    private void doSubmitCommand()
    {
        //получаем введённые данные
        String[] strings= strings=new InputParser().parse(viewImpl.inputCommand());
        try {
            //Пытаемся получить соответствующую команду из маппинг
            Commands command = Commands.valueOf(strings[0].toUpperCase());
            if (actions.containsKey(command)) {
                ActionData<String,  AsynchronousChannelRequestProperties, String[]> action = actions.get(command);
                //если команда найдена то передаём ей оставшиеся параметры
                action.accept(Arrays.copyOfRange(strings, 1, strings.length));
            }
        }catch (Exception e)
        {
            //Команда не найдена. Запрос новой команды
            viewImpl.showMessage(String.format("Command %s is not allowed",strings[0]));
            showCommands();
        }
    }
    //отображение списка команд
    private void showCommands()
    {
        this.viewImpl.showCommands(actions);
        doSubmitCommand();
    }


}
