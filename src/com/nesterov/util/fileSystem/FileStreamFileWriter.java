package com.nesterov.util.fileSystem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

//Обёртка для зписи байтов в fileOutputStream
public class FileStreamFileWriter implements BytesWriter {

    private final File file;
    private final FileChannel fileChannel=null;
    private final MappedByteBuffer buffer=null;
    private final FileOutputStream fileOutputStream;
    private int position=0;
    public FileStreamFileWriter(File file) throws IOException {
        this.file=file;
        fileOutputStream=new FileOutputStream(file);

    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public long size() {
        long size=-1;
        try {
            size=fileChannel.size();
        } catch (IOException e) {
           throw new RuntimeException(e.getMessage());
        }
        return size;
    }

    @Override
    public void writeNext(byte[] bytes) {
        try {
            fileOutputStream.write(bytes,0,bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.position+=bytes.length;
    }

    @Override
    public void close() {
        try {
            this.fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
