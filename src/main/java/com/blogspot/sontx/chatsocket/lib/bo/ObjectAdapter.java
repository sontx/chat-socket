package com.blogspot.sontx.chatsocket.lib.bo;

/**
 * Convert bytes to object and vice versa.
 */
public interface ObjectAdapter {
    byte[] objectToBytes(Object obj);

    Object bytesToObject(byte[] bytes);
}