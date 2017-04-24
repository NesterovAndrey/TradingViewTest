package com.nesterov.util.fileSystem;

//Обёртка для чтения байтов из потока
public interface BytesLoader {
    int getPosition();
    long size();
    byte[] loadNext(int count);
    boolean isLoaded();
}
