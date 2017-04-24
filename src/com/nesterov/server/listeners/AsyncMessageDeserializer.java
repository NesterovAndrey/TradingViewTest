package com.nesterov.server.listeners;

import com.nesterov.core.json.JSONMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

//TODO Generic
public final class AsyncMessageDeserializer implements MessageDeserializer<Map> {
    private final InputStream inputStream;
    private final JSONMapper jsonMapper;
    public AsyncMessageDeserializer(InputStream inputStream, JSONMapper jsonMapper)
    {
        this.inputStream=Optional.of(inputStream).get();
        this.jsonMapper=Optional.of(jsonMapper).get();
    }
    //Преобразуем полученную строку в Map
    @Override
    public synchronized Map deserialize() {
        Map<Object,List> raw=null;
        try {
            ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
            raw=jsonMapper.map((String)objectInputStream.readObject());

        } catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        //Каждому ключу соответсвуут только одно значение
        Map result=new HashMap();
        for(Object key:raw.keySet())
        {
            result.put(key,raw.get(key).get(0));
        }
        return result;
    }
}
