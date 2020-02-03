package com.blogspot.sontx.chatsocket.lib.thread;

import com.blogspot.sontx.chatsocket.lib.Function;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractThreadInvoker implements ThreadInvoker {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void executeAsync(Runnable callback) {
        executorService.execute(callback);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T invokeWithResult(Function<T> func) {
        Object[] result = new Object[1];
        invokeAndWait(() -> result[0] = func.call());
        return (T) result[0];
    }
}
