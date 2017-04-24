package com.nesterov.server.task.filesList;

import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.executions.task.TaskFactory;
import com.nesterov.server.folder.FolderFilesList;

import java.util.Optional;
import java.util.function.Consumer;

public class FilesListTaskFactory implements TaskFactory<Float,Void, RequestProperties> {
    private final FolderFilesList folderListing;
    private final TaskExceptionFactory<RequestProperties> taskExceptionFactory;
    public FilesListTaskFactory(FolderFilesList folderListing,
                                TaskExceptionFactory<RequestProperties> taskExceptionFactory)
    {
        this.folderListing= Optional.of(folderListing).get();
        this.taskExceptionFactory=Optional.of(taskExceptionFactory).get();
    }
    @Override
    public AbstractTask<Float, Void,RequestProperties> create(RequestProperties properties,
                                                              Consumer<Float> onProgress,
                                                              Consumer<Void> onComplete,
                                                              Consumer<Throwable> onFail
    ) {
        return new FilesListTask(this.folderListing, onProgress,onComplete,onFail,taskExceptionFactory);
    }
}
