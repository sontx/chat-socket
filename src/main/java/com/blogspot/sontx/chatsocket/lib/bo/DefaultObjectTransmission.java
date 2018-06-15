package com.blogspot.sontx.chatsocket.lib.bo;

import java.io.IOException;

/**
 * Object communicating based on bytes stream.
 */
public final class DefaultObjectTransmission implements ObjectTransmission {
    private static final int DATA_IN_BUFFER_SIZE = 4096;

    private final ObjectAdapter objectAdapter;
    private final ByteTransmission byteTransmission;

    public DefaultObjectTransmission(ObjectAdapter objectAdapter, ByteTransmission byteTransmission) {
        this.objectAdapter = objectAdapter;
        this.byteTransmission = byteTransmission;
    }

    public void sendObject(Object obj) throws IOException {
        byteTransmission.sendBytes(objectAdapter.objectToBytes(obj));
    }

    public Object receiveObject() throws IOException {
        byte[] objectBytes = new byte[DATA_IN_BUFFER_SIZE];
        int realLength = byteTransmission.receiveBytes(objectBytes, 0, objectBytes.length);
        if (realLength > 0) {
            byte[] realBytes = new byte[realLength];
            System.arraycopy(objectBytes, 0, realBytes, 0, realLength);
            return objectAdapter.bytesToObject(realBytes);
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        byteTransmission.close();
    }
}
