package com.nesterov.util.observer;
import java.util.Observable;
import java.util.Observer;

public final class ObservableWrapperImpl<T> extends Observable implements com.nesterov.util.observer.Observable<T> {

    public void addListener(Observer o)
    {
        super.addObserver(o);
    }
    public void deleteListeners(Observer o)
    {
        super.deleteObserver(o);
    }
    public void notifyListeners()
    {
        this.notifyListeners(null);
    }
    public void notifyListeners(T arg)
    {
        super.setChanged();
        super.notifyObservers(arg);
    }
}
