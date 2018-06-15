package com.blogspot.sontx.chatsocket.client.model;

import java.io.Closeable;

/**
 * Lowest layer that communicates with {@link com.blogspot.sontx.chatsocket.server.model.Server}.
 */
public interface Client extends Closeable {
    void start();
}
