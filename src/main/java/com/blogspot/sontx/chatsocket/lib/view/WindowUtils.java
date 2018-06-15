package com.blogspot.sontx.chatsocket.lib.view;

import javax.swing.*;

public final class WindowUtils {

    private WindowUtils() {
    }

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException ignored) {
        }
    }
}
