package com.blogspot.sontx.chatsocket.server.view.javafx;

import com.blogspot.sontx.chatsocket.lib.thread.JavaFxInvoker;
import com.blogspot.sontx.chatsocket.lib.view.AbstractViewFactory;
import com.blogspot.sontx.chatsocket.server.view.LogView;
import com.blogspot.sontx.chatsocket.server.view.MainView;

public class JavaFxViewFactory extends AbstractViewFactory {
    private MainWindow mainWindow;

    public JavaFxViewFactory() {
        register(MainView.class, this::createMainWindowIfNecessary);
        register(LogView.class, this::createMainWindowIfNecessary);
    }

    private MainWindow createMainWindowIfNecessary() {
        if (this.mainWindow == null)
            mainWindow = new JavaFxInvoker.Helper<MainWindow>().invokeWithResult(MainWindow::new);
        return mainWindow;
    }
}
