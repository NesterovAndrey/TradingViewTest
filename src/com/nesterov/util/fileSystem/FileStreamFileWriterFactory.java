package com.nesterov.util.fileSystem;

import java.io.File;
import java.io.IOException;

public class FileStreamFileWriterFactory implements BytesWriterFactory{
    @Override
    public BytesWriter create(File file) {
        BytesWriter result=null;
        try {
            result= new FileStreamFileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}
