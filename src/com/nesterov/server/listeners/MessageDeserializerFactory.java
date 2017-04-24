package com.nesterov.server.listeners;

import java.io.InputStream;

public interface MessageDeserializerFactory<T> {
    MessageDeserializer<T> create(InputStream inputStream);
}
