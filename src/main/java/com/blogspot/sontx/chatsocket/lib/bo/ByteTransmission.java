package com.blogspot.sontx.chatsocket.lib.bo;

import java.io.Closeable;
import java.io.IOException;

/**
 * Communicating based on bytes stream.
 */
public interface ByteTransmission extends Closeable {
    void sendBytes(byte[] in) throws IOException;

    int receiveBytes(byte[] out, int offset, int length) throws IOException;
}
