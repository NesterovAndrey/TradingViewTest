package com.nesterov.core.executions.properties;


import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
//Параметры цепочки вызовов команд(ExecutionChain)
public interface RequestProperties {
Object get(String key);
void put(String key,Object data);
Set<String> keys();
AsynchronousSocketChannel getSocketChannel();
void setSocketChannel(AsynchronousSocketChannel socketChannel);
InputStream getInputStream();
OutputStream getOutputStream();
}
