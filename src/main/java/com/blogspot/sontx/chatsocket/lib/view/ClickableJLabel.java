package com.blogspot.sontx.chatsocket.lib.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickableJLabel extends JLabel {
    private List<ActionListener> actionListeners = new ArrayList<>();

    public ClickableJLabel(String s) {
        super(s);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionEvent event = new ActionEvent(ClickableJLabel.this, ActionEvent.ACTION_PERFORMED, "clicked");
                for (ActionListener actionListener : actionListeners) {
                    actionListener.actionPerformed(event);
                }
            }
        });
    }

    public void addActionListener(ActionListener l) {
        actionListeners.add(l);
    }
}
