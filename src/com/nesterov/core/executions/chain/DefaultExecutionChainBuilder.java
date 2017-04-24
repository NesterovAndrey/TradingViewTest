package com.nesterov.core.executions.chain;

import com.nesterov.core.executions.builder.ExecutionBuilder;
import com.nesterov.core.executions.task.AbstractTask;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DefaultExecutionChainBuilder<CP> extends AbstractExecutionChainBuilder<String,CP>{
    public DefaultExecutionChainBuilder(CP chainProperties)
    {
        super(chainProperties);
    }
    public <P,C,V> ExecutionChainBuilder thenExecute(ExecutionBuilder<P,C, V> builder)
    {
        return thenExecute(builder,builder.toString());
    }

    public ExecutionChain build()
    {
        Queue<AbstractTask<?,?,CP>> tasks=new ConcurrentLinkedQueue<>();
        for(ExecutionBuilder executionBuilder:this.builders())
        {
            tasks.add(executionBuilder.build());
        }
        return new ExecutionChain<CP>(tasks,this.chainProperties(),this.onChainComplete,this.onChainFail);
    }


}
