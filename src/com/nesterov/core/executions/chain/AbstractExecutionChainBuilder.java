package com.nesterov.core.executions.chain;


import com.nesterov.core.executions.builder.ExecutionBuilder;
import com.nesterov.core.executions.task.TaskFactory;

import java.util.*;
import java.util.function.Consumer;
//Билдер для созданя цепочки вызова зада(ExecutionChain)
public abstract class AbstractExecutionChainBuilder <I,CP> implements ExecutionChainBuilder<I,CP> {
    protected Consumer<CP> onChainComplete;
    protected Consumer<Throwable> onChainFail;
    private final Collection<ExecutionBuilder> builders;
    private final Map<I,ExecutionBuilder> builderMap=new HashMap<>();
    private CP chainProperties;
    public AbstractExecutionChainBuilder(CP chainProperties)
    {
        builders=new LinkedList<>();
        this.chainProperties=Optional.of(chainProperties).get();

    }
    public <P,C,V> ExecutionChainBuilder thenExecute(ExecutionBuilder<P,C, V> builder, I key)
    {
        builders.add(builder);
        builderMap.put(key,builder);
        return this;
    }
    public <P,C,V> ExecutionChainBuilder thenExecute(TaskFactory<P, C, V> taskFactory, I executionBuilderName)
    {
        thenExecute(new ExecutionBuilder<>(taskFactory), executionBuilderName);
        return this;
    }

    public <P,C,V>  ExecutionBuilder<P,C, V> command(I executionBuilderName)
    {
        return builderMap.get(executionBuilderName);
    }

    @Override
    public <P,C,V> ExecutionBuilder<P,C, V> onComplete(I executionBuilderName, Consumer<C> onComplete) {
        command(executionBuilderName).onComplete((Consumer<Object>) onComplete);
        return command(executionBuilderName);
    }

    @Override
    public <P,C,V> ExecutionBuilder<P,C, V> onProgress(I executionBuilderName, Consumer<P> onProgress) {
        command(executionBuilderName).onProgress((Consumer<Object>) onProgress);
        return command(executionBuilderName);

    }

    @Override
    public <P,C,V> ExecutionBuilder <P,C,V> properties(I executionBuilderName, V properties) {
        command(executionBuilderName).properties(properties);
        return command(executionBuilderName);
    }
    @Override
    public ExecutionChainBuilder<I,CP> onChainComplete(Consumer<CP> onComplete) {
        this.onChainComplete =onComplete;
        return this;
    }

    @Override
    public ExecutionChainBuilder<I,CP> onChainFail(Consumer<Throwable> onFail) {
        this.onChainFail =onFail;
        return this;
    }

    @Override
    public ExecutionChainBuilder<I,CP> chainProperties(CP properties) {
        this.chainProperties=properties;
        return this;
    }
    protected Collection<ExecutionBuilder> builders()
    {
        return this.builders;
    }
    protected Map<I,ExecutionBuilder>builderMap()
    {
        return this.builderMap;
    }
    protected CP chainProperties()
    {
        return this.chainProperties;
    }
}
