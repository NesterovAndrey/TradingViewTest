package com.nesterov.client.view;

import com.nesterov.client.presenter.Commands;
import com.nesterov.client.presenter.input.ActionData;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//отображене данных
public class ViewImpl implements View {

    private PrintStream printStream;
    private Scanner scanner;
    public ViewImpl(PrintStream printStream,InputStream inputStream) {
        this.printStream = printStream;
        this.scanner= new Scanner(inputStream);
    }

    @Override
    public void showFiles(List filesList) {
            filesList.forEach((ob->this.printStream.println(String.format("File name:%s",ob))));
    }

    public String inputCommand() {

        String result = null;
        if(this.scanner.hasNextLine())
        result =this.scanner.nextLine();
        return result;
    }

    public void showCommands(Map<Commands, ActionData<String, AsynchronousChannelRequestProperties,String[]>> commandsMap) {
        this.printStream.println("Enter command:");

        commandsMap.keySet().stream().forEach(
                (Commands action)->{showMessage(commandsMap.get(action).buildDescription());});

    }
    @Override
    public void showMessage(String message) {
        this.printStream.println(message);
    }
}
