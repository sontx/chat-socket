package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.client.model.UserProfile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseSwingWindow;
import com.blogspot.sontx.chatsocket.lib.view.ClickableJLabel;
import com.blogspot.sontx.chatsocket.lib.view.FitImageJLabel;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ProfileWindow extends BaseSwingWindow implements ProfileView, ActionListener {
    private JTextField statusField;
    private JTextField displayNameField;
    private ClickableJLabel passwordButton;
    private ClickableJLabel displayNameButton;
    private ClickableJLabel statusButton;
    private JLabel usernameField;
    @Setter
    private Callback<String> changeDisplayNameButtonClickListener;
    @Setter
    private Callback<String> changeStatusButtonClickListener;
    @Setter
    private Callback<String> changePasswordButtonClickListener;

    @Override
    protected void initializeComponents() {
        setResizable(false);
        getContentPane().setLayout(null);

        FitImageJLabel avatarField = new FitImageJLabel();
        avatarField.setBounds(10, 11, 101, 150);
        avatarField.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("profile.png")));
        getContentPane().add(avatarField);

        displayNameField = new JTextField();
        displayNameField.setText("Handsome man");
        displayNameField.setBounds(203, 94, 164, 20);
        getContentPane().add(displayNameField);
        displayNameField.setColumns(10);

        statusField = new JTextField();
        statusField.setBounds(203, 119, 164, 20);
        getContentPane().add(statusField);
        statusField.setColumns(10);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(121, 44, 72, 14);
        getContentPane().add(lblUsername);

        usernameField = new JLabel("anonymous");
        usernameField.setFont(new Font("Tahoma", Font.BOLD, 11));
        usernameField.setBounds(203, 44, 164, 14);
        getContentPane().add(usernameField);

        JLabel lblDisplayName = new JLabel("Display name:");
        lblDisplayName.setBounds(121, 97, 72, 14);
        getContentPane().add(lblDisplayName);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(121, 122, 72, 14);
        getContentPane().add(lblStatus);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(121, 69, 72, 14);
        getContentPane().add(lblPassword);

        JLabel label = new JLabel("*******");
        label.setBounds(203, 72, 46, 14);
        getContentPane().add(label);

        passwordButton = new ClickableJLabel("Change...");
        passwordButton.setForeground(Color.BLUE);
        passwordButton.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordButton.setBounds(374, 69, 60, 14);
        passwordButton.addActionListener(this);
        getContentPane().add(passwordButton);

        displayNameButton = new ClickableJLabel("Change...");
        displayNameButton.setHorizontalAlignment(SwingConstants.RIGHT);
        displayNameButton.setForeground(Color.BLUE);
        displayNameButton.setBounds(377, 97, 57, 14);
        displayNameButton.addActionListener(this);
        getContentPane().add(displayNameButton);

        statusButton = new ClickableJLabel("Change...");
        statusButton.setHorizontalAlignment(SwingConstants.RIGHT);
        statusButton.setForeground(Color.BLUE);
        statusButton.setBounds(377, 122, 57, 14);
        statusButton.addActionListener(this);
        getContentPane().add(statusButton);

        JButton btnLookGood = new JButton("Look good");
        btnLookGood.setBounds(345, 237, 89, 23);
        btnLookGood.addActionListener(this);
        getContentPane().add(btnLookGood);

        setSize(450, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(passwordButton)) {
            PasswordDialog passwordDialog = new PasswordDialog();
            passwordDialog.setChangePasswordButtonClickListener(password -> {
                passwordDialog.dispose();
                if (changePasswordButtonClickListener != null)
                    changePasswordButtonClickListener.call(password);
            });
            passwordDialog.setVisible(true);
        } else if (e.getSource().equals(displayNameButton)) {
            if (changeDisplayNameButtonClickListener != null)
                changeDisplayNameButtonClickListener.call(displayNameField.getText());
        } else if (e.getSource().equals(statusButton)) {
            if (changeStatusButtonClickListener != null)
                changeStatusButtonClickListener.call(statusField.getText());
        } else {
            dispose();
        }
    }

    @Override
    public void setProfile(UserProfile userProfile) {
        displayNameField.setText(userProfile.getDisplayName());
        statusField.setText(userProfile.getStatus());
        usernameField.setText(userProfile.getUsername());
    }
}
