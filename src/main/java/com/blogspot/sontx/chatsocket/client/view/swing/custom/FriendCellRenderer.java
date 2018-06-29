package com.blogspot.sontx.chatsocket.client.view.swing.custom;

import com.blogspot.sontx.chatsocket.lib.view.TwoLineJLabel;

import javax.swing.*;
import java.awt.*;

public class FriendCellRenderer extends TwoLineJLabel implements ListCellRenderer<FriendEntry> {
    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    public FriendCellRenderer() {
        setOpaque(true);
        setIconTextGap(12);
    }

    public Component getListCellRendererComponent(
            @SuppressWarnings("rawtypes") JList list,
            FriendEntry value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        setText(value.getDisplayName(), value.getStatus());
        setIcon(value.getImage());

        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }

        return this;
    }
}