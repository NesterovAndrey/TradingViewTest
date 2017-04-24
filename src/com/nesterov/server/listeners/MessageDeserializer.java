package com.nesterov.server.listeners;

public interface MessageDeserializer<T> {
    T deserialize();
}
