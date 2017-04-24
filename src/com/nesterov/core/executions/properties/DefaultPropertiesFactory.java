package com.nesterov.core.executions.properties;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

import java.util.Properties;

public class DefaultPropertiesFactory implements PropertiesFactory<AsynchronousChannelRequestProperties> {
    private final String[] args;
    private final String[] keys;
    public DefaultPropertiesFactory(String[] keys, String[] args) throws WrongNumberArgsException {
        if(keys.length!=args.length) throw new WrongNumberArgsException(String.format("args length must be %d but now %d",keys.length,args.length));
        this.keys=keys;
        this.args=args;
    }
    @Override
    public  AsynchronousChannelRequestProperties create() {
        Properties properties=new Properties();
        for(int i=0;i<keys.length;i++)
        {
            properties.put(keys[i], args[i]);
        }
        return new  AsynchronousChannelRequestProperties(properties);
    }
}
