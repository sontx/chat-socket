package com.blogspot.sontx.chatsocket.lib.thread;

public interface Invoker {
    void invokeLater(Runnable runnable);
    void invokeAndWait(Runnable runnable);
}
