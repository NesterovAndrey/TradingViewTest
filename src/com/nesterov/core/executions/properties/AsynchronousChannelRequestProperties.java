package com.nesterov.core.executions.properties;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
//Параметры цепочки вызовов задач(ExecutionChain)
//Хранит данные подключения и запроса/ответа
public class AsynchronousChannelRequestProperties implements RequestProperties {
    private Map<String,Object> properties=new ConcurrentHashMap<>();
    private AsynchronousSocketChannel socketChannel;
    private InputStream inputStream;
    private OutputStream outputStream;

    public AsynchronousChannelRequestProperties()
    {

    }
    public AsynchronousChannelRequestProperties(Map properties)
    {
        properties.keySet().forEach((key)->
        {
            this.properties.put((String)key,properties.get(key));
        });
    }
    public Object get(String key)
    {
        return properties.get(key);
    }
    public void put(String key,Object data)
    {
        properties.put(key,data);
    }
    public Set<String> keys()
    {
        return properties.keySet();
    }
    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }
    public void setSocketChannel(AsynchronousSocketChannel socketChannel)
    {
        if(this.socketChannel!=null) throw new IllegalStateException("Socket channel was set earlier.");
        this.socketChannel=socketChannel;
        this.inputStream= Channels.newInputStream(this.socketChannel);
        this.outputStream=Channels.newOutputStream(this.socketChannel);
        System.out.println("SOCK "+socketChannel);
    }
    public InputStream getInputStream() {
        if(this.inputStream==null) throw new IllegalStateException("Socket channel must be set before calling InputStream");
        return inputStream;
    }
    public OutputStream getOutputStream() {
        if(this.outputStream==null) throw new IllegalStateException("Socket channel must be set before calling OutputStream");
        return outputStream;
    }
}
