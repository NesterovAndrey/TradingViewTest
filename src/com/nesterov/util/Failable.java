package com.nesterov.util;

public interface Failable<E> {
    void onFail(E arg);
}
