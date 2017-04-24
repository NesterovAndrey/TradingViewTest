package com.nesterov.util.fileSystem;

import java.io.File;
import java.io.IOException;

public class MappedFileBytesLoaderFactory implements BytesLoaderFactory {
    @Override
    public BytesLoader create(File file) {
        BytesLoader result=null;
        try {
            result= new MappedFileBytesReader(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
