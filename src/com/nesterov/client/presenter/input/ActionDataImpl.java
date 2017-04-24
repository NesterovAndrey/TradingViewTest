package com.nesterov.client.presenter.input;

import com.nesterov.client.presenter.Commands;
import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;
import com.nesterov.core.executions.properties.DefaultPropertiesFactory;
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

import java.util.Arrays;
import java.util.function.Consumer;

//Данные для создания команды командной строки
public class ActionDataImpl implements ActionData<String,AsynchronousChannelRequestProperties,String[]> {

    private final Commands command;
    private final String description;
    private final String[] args;
    private final Consumer<AsynchronousChannelRequestProperties> action;

    public ActionDataImpl(Commands command, String description, String[] args,Consumer<AsynchronousChannelRequestProperties> action)
    {
        this.command=command;
        this.description=description;
        this.args=args;
        this.action=action;
    }
    public String buildDescription()
    {
        return String.format("Command: %s" +
                        "\n\tArguments: %s" +
                        "\n\tDescription: %s", command.toString().toLowerCase(), Arrays.toString(this.args),this.description);
    }
    public AsynchronousChannelRequestProperties buildArguments(String[] args) {
        try {
            return new DefaultPropertiesFactory(this.args,args).create();
        } catch (WrongNumberArgsException e) {
            throw new RuntimeException("Arguments count must be equals");
        }
    }
    public void accept(String[] args)
    {
        this.action.accept(buildArguments(args));
    }

}
