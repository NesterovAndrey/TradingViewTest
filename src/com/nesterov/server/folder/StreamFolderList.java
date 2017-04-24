package com.nesterov.server.folder;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class StreamFolderList implements FolderFilesList{
    private final Path folder;
    public StreamFolderList(Path folder)
    {
        this.folder=folder;
    }
    @Override
    public Collection<File> filesList() {
        Collection<File> list=new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.folder)) {
            stream.forEach((Path path)->{
                File file=path.toFile();
                if(file.isFile())
                list.add(path.toFile());
            });
        } catch (IOException e) {
            throw new RuntimeException(String.format("Folder %s not found",this.folder.toString()));
        }
        return Collections.unmodifiableCollection(list);
    }
}
