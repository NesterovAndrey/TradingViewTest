package com.nesterov.core.executions.task;

import com.nesterov.core.executions.chain.ExecutionChain;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Задача цеопчки выполнения задач
 * @param <P> Тип параметра передаваемый в коллбек onProgress
 * @param <C> Тип параметра передаваемый в коллбек onComplete
 * @param <V> Тип свойств цепочки выполнения задач передаваемых в Invoke
 */
public abstract class AbstractTask<P,C, V> implements Task<P,V>{

    private final Consumer<P> onProgress;
    private final Consumer<C> onComplete;
    private final Consumer<Throwable> onFail;
    private final TaskExceptionFactory<V> exceptionFactory;

    /*public AbstractTask(Consumer<P> onProgress, Consumer<C> onComplete, Consumer<Throwable> onFail)
    {
        this(onProgress,onComplete,onFail,null);
    }*/
    public AbstractTask(Consumer<P> onProgress, Consumer<C> onComplete, Consumer<Throwable> onFail,TaskExceptionFactory<V> exceptionFactory)
    {
        //Заполняем колбеки данными по умолчанию если они не предоставлены
        this.onProgress=Optional.ofNullable(onProgress).orElse((p)->{});
        this.onComplete=Optional.ofNullable(onComplete).orElse((c)->{});
        this.onFail=Optional.ofNullable(onFail).orElse((E)->{});
        this.exceptionFactory=Optional.ofNullable(exceptionFactory).orElse(new DefaultTaskExceptionFactory<V>());
    }
    //выполняем задачу
    public void invoke(ExecutionChain<V> chain, V requestProperties)
    {
        C result=null;
        try{
            result=execute(chain,requestProperties);
            this.onComplete.accept(result);
            //Пр удачном выполнении переходим дальше
            chain.doNext(requestProperties);

        }
        catch (Throwable e)
        {
           callOnFail(e,requestProperties);
        }

    }

    /**
     * Метод вызываемый при работе ExecutionChain
     * @param chain Конкретный объект ExecutionChain в котором работет AbstractTask
     * @param requestProperties свойства ExecutionChain передающиеся всем задачам
     * @throws Exception Перехватываем все исключения произошедшие в execute и передаём в callOnFail
     * @return
     */
    abstract protected C execute(ExecutionChain<V> chain, V requestProperties) throws Exception;
    protected void callOnFail(Throwable e,V requestProperties)
    {
        TaskException exception=this.exceptionFactory.create(e,requestProperties);
        this.onFail.accept(exception);
        throw exception;
    }
    public void updateProgress(P progress)
    {
        this.onProgress.accept(progress);
    }

}
