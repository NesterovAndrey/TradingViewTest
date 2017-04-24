package com.nesterov.util.fileSystem;

import java.io.File;

public interface BytesLoaderFactory {
    BytesLoader create(File file);
}
