package com.blogspot.sontx.chatsocket.lib.view;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public final class MessageBox {
    public static final int MESSAGE_ERROR = 1;
    public static final int MESSAGE_INFO = 2;

    @Setter
    private static Invoker invoker;

    private MessageBox() {
    }

    public static void showInUIThread(
            Component parentComponent,
            Object message,
            int messageType) {

        if (invoker != null) {
            invoker.invokeLater(() -> show(parentComponent, message, messageType));
        }
    }

    private static ImageIcon getImageIconByType(int type) {
        switch (type) {
            case MESSAGE_ERROR:
                return new ImageIcon(ImagesResource.getInstance().getImageByName("msgbox-error.png"));
            case MESSAGE_INFO:
                return new ImageIcon(ImagesResource.getInstance().getImageByName("msgbox-info.png"));
        }
        return null;
    }

    public static void show(final Component parentComponent, final Object message, int messageType) {
        JLabel labelMessage = new JLabel(message.toString(), getImageIconByType(messageType), SwingConstants.CENTER);
        final JComponent[] components = new JComponent[]{labelMessage};
        JOptionPane.showMessageDialog(parentComponent, components, "Message", JOptionPane.PLAIN_MESSAGE);
    }
}
