package com.nesterov.server.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
//Класс для управления сервисами
public class ServiceContainerImpl implements ServiceContainer {
    private final ExecutorService executorService;
    private Map<String,Service> serviceMap=new HashMap<>();
    public ServiceContainerImpl(ExecutorService executorService)
    {
        this.executorService=executorService;
    }
    @Override
    public void addService(Service service) {
        addService(Integer.toString(service.hashCode()),service);
    }

    @Override
    public void addService(String name, Service service) {
        serviceMap.put(name,service);
        executorService.submit(service);

    }
    @Override
    public void stop() {
        serviceMap.forEach((s, service) -> service.stop());
    }
}
