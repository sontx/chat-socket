package com.blogspot.sontx.chatsocket.lib.service.message;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;

import javax.swing.*;

public class SwingMessageBox implements MessageBox {
    @Override
    public void show(String caption, String text, MessageType type) {
        JLabel labelMessage = new JLabel(text, getImageIconByType(type), SwingConstants.CENTER);
        final JComponent[] components = new JComponent[]{labelMessage};
        JOptionPane.showMessageDialog(null, components, caption, JOptionPane.PLAIN_MESSAGE);
    }

    private ImageIcon getImageIconByType(MessageType type) {
        switch (type) {
            case Error:
                return new ImageIcon(ImagesResource.getInstance().getImageByName("msgbox-error.png"));
            case Info:
                return new ImageIcon(ImagesResource.getInstance().getImageByName("msgbox-info.png"));
        }
        return null;
    }
}
