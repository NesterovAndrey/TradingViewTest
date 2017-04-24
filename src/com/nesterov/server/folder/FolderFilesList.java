package com.nesterov.server.folder;

import java.io.File;
import java.util.Collection;
//Интерфейс для полученя списка файлов
public interface FolderFilesList {
    Collection<File> filesList();
}
