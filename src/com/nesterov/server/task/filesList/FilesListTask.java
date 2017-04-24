package com.nesterov.server.task.filesList;

import com.nesterov.StringConstants;
import com.nesterov.core.executions.chain.ExecutionChain;
import com.nesterov.core.executions.properties.RequestProperties;
import com.nesterov.core.executions.task.AbstractTask;
import com.nesterov.core.executions.task.TaskExceptionFactory;
import com.nesterov.core.json.JsonBuilder;
import com.nesterov.server.folder.FolderFilesList;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Получает список файлов и сохраняет их в параметры цепи
 */
public class FilesListTask extends AbstractTask<Float,Void, RequestProperties> {

    private final FolderFilesList folderListing;
    protected FilesListTask(FolderFilesList folderListing,
                         Consumer<Float> onProgress,
                         Consumer<Void> onComplete,
                         Consumer<Throwable> onFail,
                         TaskExceptionFactory<RequestProperties> exceptionFactory) {
        super(onProgress, onComplete,onFail,exceptionFactory);
        this.folderListing=folderListing;
    }

    @Override
    public Void execute(ExecutionChain<RequestProperties> chain, RequestProperties requestProperties) throws Exception {
        Collection<File> files=this.folderListing.filesList();
        //Сохраняем полученные данные в параметры цепи
        requestProperties.put(StringConstants.RESPONSE_DATA,getData(files));
        return null;
    }
    private String getData(Collection<File> files)
    {
        JsonBuilder jsonBuilder=new JsonBuilder();
        for(File file:files)
        {
            jsonBuilder.append("filename",file.getName());
        }
        return jsonBuilder.build();
    }
}
