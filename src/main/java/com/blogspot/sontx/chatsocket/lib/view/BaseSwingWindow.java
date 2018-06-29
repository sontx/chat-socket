package com.blogspot.sontx.chatsocket.lib.view;

import javax.swing.*;
import java.awt.event.WindowEvent;

public abstract class BaseSwingWindow extends JFrame implements BaseView {

    private Runnable onClosingListener;

    protected BaseSwingWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeComponents();
    }

    protected abstract void initializeComponents();

    public void setMainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
