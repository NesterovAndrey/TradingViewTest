package com.nesterov.client.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Connection<T> {
    T connect(InetSocketAddress address) throws IOException, InterruptedException, ExecutionException, TimeoutException;
}
