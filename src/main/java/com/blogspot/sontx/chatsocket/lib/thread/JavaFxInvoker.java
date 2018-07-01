package com.blogspot.sontx.chatsocket.lib.thread;

import com.blogspot.sontx.chatsocket.lib.Function;
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

    @Override
    public <T> T invokeWithResult(Function<T> func) {
        return new Helper<T>().invokeWithResult(func);
    }

    public static class Helper<T> {
        private final Object sync = new Object();
        private T result;

        public T invokeWithResult(Function<T> func) {
            return Platform.isFxApplicationThread() ? func.call() : runInJavaFxAppThread(func);
        }

        private T runInJavaFxAppThread(Function<T> func) {
            Platform.runLater(() -> {
                result = func.call();
                synchronized (sync) {
                    sync.notify();
                }
            });

            try {
                synchronized (sync) {
                    sync.wait();
                }
            } catch (InterruptedException e) {
                log.error("Error while invoking method", e);
            }

            return result;
        }
    }
}
