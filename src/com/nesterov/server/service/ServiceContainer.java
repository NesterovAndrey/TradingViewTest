package com.nesterov.server.service;

public interface ServiceContainer{
    void addService(Service service);
    void addService(String name,Service service);
    void stop();
}
