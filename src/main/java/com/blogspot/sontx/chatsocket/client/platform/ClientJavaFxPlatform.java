package com.blogspot.sontx.chatsocket.client.platform;

import com.blogspot.sontx.chatsocket.client.view.javafx.JavaFxViewFactory;
import com.blogspot.sontx.chatsocket.lib.platform.JavaFxPlatform;
import com.blogspot.sontx.chatsocket.lib.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;

public class ClientJavaFxPlatform extends JavaFxPlatform {
    private static final Object lock = new Object();
    @Getter
    private final ViewFactory viewFactory;

    public ClientJavaFxPlatform() {
        viewFactory = new JavaFxViewFactory();
        startEntryApp();
    }

    private void startEntryApp() {
        new Thread(() -> Application.launch(App.class)).start();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class App extends Application {

        @Override
        public void start(Stage primaryStage) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
