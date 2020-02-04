package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PasswordDialog extends JDialog implements ActionListener {
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JButton btnCancel;
    @Setter
    private Callback<UpdatePassword> changePasswordButtonClickListener;

    PasswordDialog() {
        setResizable(false);
        setTitle("Change Password");
        getContentPane().setLayout(null);

        JLabel lblCurrentPassword = new JLabel("Current password:");
        lblCurrentPassword.setBounds(10, 14, 74, 14);
        getContentPane().add(lblCurrentPassword);

        JLabel lblNewPassword = new JLabel("New password:");
        lblNewPassword.setBounds(10, 41, 74, 14);
        getContentPane().add(lblNewPassword);

        currentPasswordField = new JPasswordField();
        currentPasswordField.setBounds(94, 11, 224, 20);
        getContentPane().add(currentPasswordField);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(94, 38, 224, 20);
        getContentPane().add(newPasswordField);

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
            UpdatePassword updatePassword = new UpdatePassword();
            updatePassword.setNewPassword(new String(newPasswordField.getPassword()));
            updatePassword.setOldPassword(new String(currentPasswordField.getPassword()));
            changePasswordButtonClickListener.call(updatePassword);
        }
    }
}
