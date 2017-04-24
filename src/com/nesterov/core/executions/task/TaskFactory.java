package com.nesterov.core.executions.task;

import java.util.Properties;
import java.util.function.Consumer;

/**
 * Передаётся в ExecutionBuilder
 * Создаёт конкретную реализацию AsyncTask
 * @param <P> Тип параметра передаваемый в коллбек onProgress
 * @param <C> Тип параметра передаваемый в коллбек onComplete
 * @param <V> Тип свойств цепочки выполнения задач передаваемых в Invoke
 */
public interface TaskFactory<P,C, V> {
    AbstractTask<P,C,V> create(V properties, Consumer<P> onProgress, Consumer<C> onComplete, Consumer<Throwable> onFail);
}
