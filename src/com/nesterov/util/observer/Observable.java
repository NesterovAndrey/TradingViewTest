package com.nesterov.util.observer;

import java.util.Observer;

public interface Observable<T> {
    void addListener(Observer o);
    void deleteListeners(Observer o);
    void notifyListeners();
    void notifyListeners(T arg);
}
