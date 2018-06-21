package com.blogspot.sontx.chatsocket.lib.thread;

import javafx.application.Platform;
import lombok.extern.log4j.Log4j;

@Log4j
public class JavaFxInvoker implements Invoker {
    @Override
    public void invokeLater(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    public void invokeAndWait(Runnable runnable) {
        final Object wait = new Object();
        Platform.runLater(() -> {
            runnable.run();
            synchronized (runnable) {
                wait.notify();
            }
        });

        try {
            synchronized (runnable) {
                wait.wait();
            }
        } catch (InterruptedException e) {
            log.error("Error while invoking method", e);
        }
    }
}
