package com.nesterov.util.fileSystem;

//Обёртка для записи байтов в поток
public interface BytesWriter {
    int getPosition();
    long size();
    void writeNext(byte[] bytes);
    void close();
}
