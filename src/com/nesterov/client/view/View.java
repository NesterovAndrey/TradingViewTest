package com.nesterov.client.view;

import com.nesterov.client.presenter.Commands;
import com.nesterov.client.presenter.input.ActionData;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;

import java.util.List;
import java.util.Map;

public interface View{
    void showFiles(List files);
    String inputCommand();
    void showCommands(Map<Commands, ActionData<String, AsynchronousChannelRequestProperties,String[]>> commandsMap);
    void showMessage(String message);
}
