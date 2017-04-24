package com.nesterov.server.net;

import java.io.IOException;

public interface Connection {
    void connect() throws IOException, InterruptedException;
    void shutdown() throws InterruptedException;
}
