package com.nesterov.util.fileSystem;

import java.io.File;

public interface BytesWriterFactory {
    BytesWriter create(File file);
}
