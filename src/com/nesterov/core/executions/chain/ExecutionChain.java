package com.nesterov.core.executions.chain;


import com.nesterov.core.executions.task.AbstractTask;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
//Цепочка вызово задач
public class ExecutionChain<T> implements Runnable,Chain<T> {
    private final Consumer<T> onComplete;
    private final Consumer<Throwable> onFail;
    private final Queue<AbstractTask<?,?,T>> tasks;
    private final T chainProperties;
    private Boolean completed=false;
    public ExecutionChain(Queue<AbstractTask<?,?,T>> tasks, T properties, Consumer<T> onComplete, Consumer<Throwable> onFail) {
        this.tasks=tasks;
        this.chainProperties=properties;
        this.onComplete=Optional.ofNullable(onComplete).orElse((result)->{});
        this.onFail=Optional.ofNullable(onFail).orElse((e)->{});
    }
    //Вызов следующей задачи
    public void doNext(T properties)
    {
        try {

            if (tasks.peek()!=null) {
                tasks.poll().invoke(this, properties);
            }
            else this.onComplete.accept(properties);

        }
        catch (Throwable e)
        {
            this.onFail.accept(e);
        }
    }
    @Override
    public void run() {
        doNext(this.chainProperties);
    }
}
