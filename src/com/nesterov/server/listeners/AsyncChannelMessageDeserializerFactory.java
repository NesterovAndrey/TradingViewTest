package com.nesterov.server.listeners;

import com.nesterov.server.ServerConfig;

import java.io.InputStream;
import java.util.Map;
//Преобразует полученные от клиента данные.
public class AsyncChannelMessageDeserializerFactory implements MessageDeserializerFactory<Map> {
    @Override
    public MessageDeserializer<Map> create(InputStream inputStream) {
        return new AsyncMessageDeserializer(inputStream, ServerConfig.jsonMapper);
    }
}
