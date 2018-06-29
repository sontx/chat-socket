package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.lib.Callback;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PasswordDialog extends JDialog implements ActionListener {
    private JPasswordField passwordField;
    private JPasswordField rePasswordField;
    private JButton btnCancel;
    @Setter
    private Callback<String> changePasswordButtonClickListener;

    PasswordDialog() {
        setResizable(false);
        setTitle("Change Password");
        getContentPane().setLayout(null);

        JLabel lblNewPassword = new JLabel("New password:");
        lblNewPassword.setBounds(10, 14, 74, 14);
        getContentPane().add(lblNewPassword);

        JLabel lblConfirm = new JLabel("Confirm:");
        lblConfirm.setBounds(10, 41, 74, 14);
        getContentPane().add(lblConfirm);

        passwordField = new JPasswordField();
        passwordField.setBounds(94, 11, 224, 20);
        getContentPane().add(passwordField);

        rePasswordField = new JPasswordField();
        rePasswordField.setBounds(94, 38, 224, 20);
        getContentPane().add(rePasswordField);

        JButton btnLookGood = new JButton("Look good");
        btnLookGood.setBounds(130, 138, 89, 23);
        btnLookGood.addActionListener(this);
        getContentPane().add(btnLookGood);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(229, 138, 89, 23);
        btnCancel.addActionListener(this);
        getContentPane().add(btnCancel);

        setSize(335, 200);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnCancel)) {
            dispose();
        } else {
            if (!checkPasswordInput()) {
                changePasswordButtonClickListener.call(new String(passwordField.getPassword()));
            }
        }
    }

    private boolean checkPasswordInput() {
        String password = new String(passwordField.getPassword());
        String rePassword = new String(rePasswordField.getPassword());
        return !StringUtils.isEmpty(password) && password.equals(rePassword);
    }
}
