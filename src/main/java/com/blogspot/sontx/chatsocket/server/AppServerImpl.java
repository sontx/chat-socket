package com.blogspot.sontx.chatsocket.server;

import com.blogspot.sontx.chatsocket.server.ui.Homechat;

import javax.swing.*;

public class AppServerImpl implements AppServer {
    @Override
    public void start() throws Exception {
        setSystemLookAndFeel();
        new Homechat().setVisible(true);
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ignored) {
        }
    }
}
