package com.nesterov.util.fileSystem;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

//Memory mapped обёртка для чтения байтов
public class MappedFileBytesReader implements BytesLoader {

    private final File file;
    private final FileChannel fileChannel;
    private final MappedByteBuffer buffer;
    private int position=0;
    public MappedFileBytesReader(File file) throws IOException {
        this.file=file;
        fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
        buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, this.position, fileChannel.size());
        buffer.isLoaded();
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
            e.printStackTrace();
        }
        return size;
    }
    @Override
    public boolean isLoaded()
    {
        return this.position==size();
    }

    @Override
    public byte[] loadNext(int maxCount) {
        maxCount= buffer.position()+maxCount>size()? (int) (size() - buffer.position()) :maxCount;
        byte[] result=new byte[maxCount];
        buffer.get(result);
        this.position+=maxCount;
        return result;
    }
}
