package com.blogspot.sontx.chatsocket.lib;

public class NotRegisteredException extends RuntimeException {
    public NotRegisteredException() {
    }

    public NotRegisteredException(String msg) {
        super(msg);
    }
}
