package com.blogspot.sontx.chatsocket.server.model;

import java.io.Closeable;

/**
 * Communication server that will wait for connections from clients and routes these connections
 * to {@link Worker}.
 */
public interface Server extends Closeable {
    void start();
}
