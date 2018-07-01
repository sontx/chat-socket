package com.blogspot.sontx.chatsocket.lib.thread;

import com.blogspot.sontx.chatsocket.lib.Function;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

@Log4j
public class SwingInvoker implements Invoker {
    @Override
    public void invokeLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public void invokeAndWait(Runnable runnable) {
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException | InvocationTargetException e) {
            log.error("Error while invoking method in UI thread", e);
        }
    }

    @Override
    public <T> T invokeWithResult(Function<T> func) {
        return func.call();
    }
}
