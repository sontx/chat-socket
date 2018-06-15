package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseWindow;
import com.blogspot.sontx.chatsocket.lib.view.FitImageJLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends BaseWindow implements RegisterView, ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField displayNameField;
    private JButton btnCancel;
    private Runnable registerButtonClickListener;

    @Override
    protected void initializeComponents() {
        setResizable(false);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(129, 29, 77, 14);
        panel.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(216, 26, 188, 20);
        panel.add(usernameField);
        usernameField.requestFocus();
        usernameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(129, 62, 77, 14);
        panel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(216, 59, 188, 20);
        panel.add(passwordField);

        JLabel lblDisplayName = new JLabel("Display name:");
        lblDisplayName.setBounds(129, 124, 77, 14);
        panel.add(lblDisplayName);

        displayNameField = new JTextField();
        displayNameField.setBounds(216, 121, 188, 20);
        panel.add(displayNameField);
        displayNameField.setColumns(10);

        JButton btnTake = new JButton("Take");
        btnTake.setBounds(216, 195, 89, 23);
        btnTake.addActionListener(this);
        panel.add(btnTake);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(315, 195, 89, 23);
        btnCancel.addActionListener(this);
        panel.add(btnCancel);

        FitImageJLabel avatarField = new FitImageJLabel();
        avatarField.setBounds(10, 26, 109, 109);
        avatarField.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("avatar.png")));
        panel.add(avatarField);

        setSize(430, 264);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnCancel)) {
            dispose();
        } else {
            if (registerButtonClickListener != null)
                registerButtonClickListener.run();
        }
    }

    @Override
    public void setRegisterButtonClickListener(Runnable listener) {
        this.registerButtonClickListener = listener;
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    @Deprecated
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public String getDisplayName() {
        return displayNameField.getText();
    }
}
