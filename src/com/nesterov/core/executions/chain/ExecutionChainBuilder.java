package com.nesterov.core.executions.chain;

import com.nesterov.core.executions.builder.ExecutionBuilder;
import com.nesterov.core.executions.task.TaskFactory;

import java.util.function.Consumer;

/**
 * Задаём параметры для цепочки вызовов задач
 * Создаёт цепочку выполненя задач (ExecutionChain) из хранящихся в нём
 *билдеров задач
 * @param <T> Тип ключа с которым будут создавать маппинги для ExecutionBuilder
 * @param <V> Тип параметров цепи вызоваов передаваемый в ExecutionChain::doNext
 */
public interface ExecutionChainBuilder<T,V>  {
    /**
     * Автоматически создаёт ExecutionBuilder с переданным taskFactory
     * Добавляет билдер задачи в билдер цепочки вызовов задач
     * @param taskFactory Фабрика создающая AbstractTask
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionChainBuilder<T,V> thenExecute(TaskFactory<P, C, V> taskFactory, T executionBuilderName);

    /**
     * Добавляет билдер задачи в билдер цепочки вызовов задач
     * @param builder Билдер для создания задачи(AsyncTask)
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionChainBuilder<T,V> thenExecute(ExecutionBuilder<P,C, V> builder, T executionBuilderName);

    /**
     * Получает билдер задачи по ключу из билдера цепочки вызовов задач
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionBuilder<P,C, V> command(T executionBuilderName);

    /**
     * Коллбек для успешного выполнения цепочки вызовов задач
     * @param onComplete коллбек
     * @return
     */
    ExecutionChainBuilder<T,V> onChainComplete(Consumer<V> onComplete);

    /**
     * Коллбек для обратоки исключительной ситуации в ходе выполнения цепочки задач
     * @param onFail коллбек
     * @return
     */
    ExecutionChainBuilder<T,V> onChainFail(Consumer<Throwable> onFail);

    /**
     *
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param onComplete Коллбек для успешного выполнения задачи
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionBuilder<P,C, V> onComplete(T executionBuilderName, Consumer<C> onComplete);

    /**
     *
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param onProgress Коллбек для отслеживания прогресса выполнения задачи
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionBuilder<P,C, V> onProgress(T executionBuilderName, Consumer<P> onProgress);

    /**
     *
     * @param executionBuilderName ключ по которому можно будет получить билдер
     * @param properties свойства передаваемые в TaskFactory при создании задачи
     * @param <P> Значение передаваемое в колбек при обновлении прогресса onProgress
     * @param <C> Значене передаваемое в колбек при успешном завершении задачи onComplete
     * @param <V> Класс параметров цепи выполнения задач передаваемый в ExecutionChain::doNext
     * @return
     */
    <P,C,V> ExecutionBuilder<P,C, V> properties(T executionBuilderName, V properties);

    /**
     *
     * @param properties Свойства для цепочки выполнения задач которые будут переданы в ExecutionChain::doNext
     * @return
     */
    ExecutionChainBuilder<T,V> chainProperties(V properties);

    /**
     * Конструирует объект ExecutionChain
     * @return
     */
    ExecutionChain build();
}
