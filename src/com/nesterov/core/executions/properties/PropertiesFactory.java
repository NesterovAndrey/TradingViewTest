package com.nesterov.core.executions.properties;


import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

public interface PropertiesFactory<T> {
    public T create() throws WrongNumberArgsException;
}
