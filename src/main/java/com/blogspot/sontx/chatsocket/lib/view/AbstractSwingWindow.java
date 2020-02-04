package com.blogspot.sontx.chatsocket.lib.view;

import javax.swing.*;
import java.awt.event.WindowEvent;

public abstract class AbstractSwingWindow extends JFrame implements BaseView {
    private static AbstractSwingWindow mainWindow;

    private Runnable onClosingListener;

    protected AbstractSwingWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeComponents();
    }

    protected abstract void initializeComponents();

    @Override
    public void setMainWindow() {
        if (mainWindow != null) {
            mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
        mainWindow = this;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public boolean isMainWindow() {
        return mainWindow == this;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (onClosingListener != null)
                onClosingListener.run();
        }
        super.processWindowEvent(e);
    }

    @Override
    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void closeWindow() {
        dispose();
    }

    @Override
    public void setOnClosingListener(Runnable listener) {
        this.onClosingListener = listener;
    }
}
