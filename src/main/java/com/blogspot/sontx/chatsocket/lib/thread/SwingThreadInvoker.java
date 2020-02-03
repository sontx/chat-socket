package com.blogspot.sontx.chatsocket.lib.thread;

import lombok.extern.log4j.Log4j;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

@Log4j
public class SwingThreadInvoker extends AbstractThreadInvoker {
    @Override
    public void invokeLater(Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            executeAsync(() -> invokeLater(runnable));
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    @Override
    public void invokeAndWait(Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException | InvocationTargetException e) {
                log.error("Error while invoking method in UI thread", e);
            }
        }
    }
}
