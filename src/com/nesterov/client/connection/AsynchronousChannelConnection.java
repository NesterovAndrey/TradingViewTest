package com.nesterov.client.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsynchronousChannelConnection implements Connection<AsynchronousSocketChannel> {

    private final AsynchronousChannelGroup group;
    public  AsynchronousChannelConnection(AsynchronousChannelGroup group) throws IOException {
        this.group=group;

    }
    @Override
    public AsynchronousSocketChannel connect(InetSocketAddress address) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        AsynchronousSocketChannel result=null;
            result=AsynchronousSocketChannel.open(this.group);
            Future connecting=result.connect(address);
            connecting.get(5, TimeUnit.SECONDS);
        return result;
    }
}
