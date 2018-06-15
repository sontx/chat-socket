package com.blogspot.sontx.chatsocket.lib.bo;

import java.io.Closeable;
import java.io.IOException;

/**
 * Communicating based on object stream.
 */
public interface ObjectTransmission extends Closeable {
    void sendObject(Object obj) throws IOException;

    Object receiveObject() throws IOException;
}
