package com.blogspot.sontx.chatsocket.lib.thread;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j;

@Log4j
public class JavaFxThreadInvoker extends AbstractThreadInvoker {
    @Override
    public void invokeLater(Runnable runnable) {
        if (PlatformImpl.isFxApplicationThread()) {
            executeAsync(() -> invokeLater(runnable));
        } else {
            Platform.runLater(runnable);
        }
    }

    @Override
    public void invokeAndWait(Runnable runnable) {
        PlatformImpl.runAndWait(runnable);
    }
}
