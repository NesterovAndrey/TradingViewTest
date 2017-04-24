package com.nesterov.core.executions.builder;

import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskFactory;

import java.util.function.Consumer;

/**
 * Заполняем параметры билдера задачи из цепочки вызовов задач(ExecutionChain)
 * Используется для создания конкретной команды(AbstractTask) для цепочки вызовов команд
 * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
 * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
 * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
 */
public class ExecutionBuilder<P,C, V> {
    private final TaskFactory<P,C,V> taskFactory;
    private Consumer<C> onComplete;
    private Consumer<P> onProgress;
    private Consumer<Throwable> onFail;

    private V properties;

    /**
     * Присваивает билдеру фабрику для создания задачи
     * @param taskFactory
     */
    public ExecutionBuilder(TaskFactory<P,C,V> taskFactory)
    {
        this.taskFactory=taskFactory;
    }

    /**
     * Устанавливает коллбек успешного выполнения задачи
     * @param onComplete
     * @return
     */
    public ExecutionBuilder<P,C,V> onComplete(Consumer<C> onComplete)
    {
        this.onComplete=onComplete;
        return this;
    }

    /**
     * Устанавливает коллбек обновления прогресса
     * @param onProgress
     * @return
     */
    public ExecutionBuilder<P,C,V> onProgress(Consumer<P> onProgress)
    {
        this.onProgress=onProgress;
        return this;
    }

    /**
     * Устанавливает коллбек для аварийного завершения задачи
     * @param onFail
     * @return
     */
    public ExecutionBuilder<P,C,V> onFail(Consumer<Throwable> onFail)
    {
        this.onFail=onFail;
        return this;
    }

    /**
     * Устанавливает свойства передаваемые в TaskFactory при конструировании объекта
     * @param properties
     * @return
     */
    public ExecutionBuilder<P,C,V> properties(V properties)
    {
        this.properties=properties;
        return this;
    }

    /**
     * конструирует объект
     * @return
     */
    public AbstractTask<P,C,V> build()
    {
        return this.taskFactory.create(this.properties,onProgress,onComplete,onFail);
    }
}
