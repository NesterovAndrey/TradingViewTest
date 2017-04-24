package com.nesterov.client.presenter.input;

//Данные для создания команды командной строки
public interface ActionData<D,A,C> {
    D buildDescription();
    A buildArguments(String[] args);
    void accept(C args);
}
