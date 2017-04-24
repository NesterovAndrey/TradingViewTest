package com.nesterov.core.executions.task;

import com.nesterov.core.executions.properties.AsynchronousChannelRequestProperties;

import java.io.IOException;

/**
 * Created by yalta on 23.04.2017.
 */
public class AsynchronousChannelExceptionFactory implements TaskExceptionFactory<AsynchronousChannelRequestProperties> {

    @Override
    public TaskException create(Throwable e, AsynchronousChannelRequestProperties requestPropeties) {
        TaskException exception=null;
        try {
           exception=new TaskException(String.format("%s\n%s",e.getMessage(),requestPropeties.getSocketChannel().getRemoteAddress().toString()));
        } catch (IOException e1) {
           exception=new TaskException(String.format("%s\nCant get remote address",e.getMessage()));
        }
        return exception;
    }
}
