package com.blogspot.sontx.chatsocket.lib.thread;

import com.blogspot.sontx.chatsocket.lib.Function;

public interface ThreadInvoker {
    void invokeLater(Runnable runnable);

    void invokeAndWait(Runnable runnable);

    <T> T invokeWithResult(Function<T> func);

    void executeAsync(Runnable callback);
}
