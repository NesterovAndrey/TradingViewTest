package com.nesterov.util;

public interface Completable<C> {
    void onComplete(C param);
}
