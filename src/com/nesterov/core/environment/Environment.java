package com.nesterov.core.environment;

import sun.tools.jar.CommandLine;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;

public class Environment {
    private Formatter formatter=new Formatter();

    private Properties properties=new Properties();
    private static Environment environmentInstance=new Environment();

    private Environment(){

    }
    public <T> T getPropertie(String key)
    {
        return (T) properties.get(key);
    }
    public void addPropertie(String key,Object value)
    {
        properties.put(key,value);
    }
    public Formatter getFormatter() {
        return new Formatter();
    }

    synchronized static public  Environment getEnvironmentInstance()
    {
        return environmentInstance;
    }
    public static void fillEnvironment(String key,String arg,Object defaultValue, List<String> args)
    {
        Object val=defaultValue;
        if(args.contains(arg))
        {
            try {
                val = args.get(args.indexOf(arg) + 1);
            }
            catch (IndexOutOfBoundsException e)
            {
                System.out.println(String.format("Arguments are wrong %s",arg));
            }
        }
        getEnvironmentInstance().addPropertie(key,val);
    }
    public static <T> T getEnvironmentPropertie(String key)
    {
        return (T) getEnvironmentInstance().properties.get(key);
    }
    public static void addEnvironmentPropertie(String key,Object value)
    {
        getEnvironmentInstance().properties.put(key,value);
    }

}
