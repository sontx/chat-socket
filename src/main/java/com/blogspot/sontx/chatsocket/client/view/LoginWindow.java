package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseWindow;
import com.blogspot.sontx.chatsocket.lib.view.FitImageJLabel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends BaseWindow implements LoginView, ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton btnRegister;
    private Runnable loginButtonClickListener;
    private Runnable registerButtonClickListener;

    @Override
    protected void initializeComponents() {
        setResizable(false);
        getContentPane().setLayout(null);

        FitImageJLabel lblNewLabel = new FitImageJLabel();
        lblNewLabel.setBounds(10, 11, 340, 247);
        lblNewLabel.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("login-banner.png")));
        getContentPane().add(lblNewLabel);

        usernameField = new JTextField();
        usernameField.setBounds(451, 92, 112, 20);
        getContentPane().add(usernameField);
        usernameField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(451, 123, 112, 20);
        getContentPane().add(passwordField);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(384, 95, 57, 14);
        getContentPane().add(lblUsername);
        lblUsername.requestFocus();

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(384, 126, 57, 14);
        getContentPane().add(lblPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(451, 161, 112, 23);
        btnLogin.addActionListener(this);
        getContentPane().add(btnLogin);

        JLabel lblDontHaveAn = new JLabel("Don't have an account?");
        lblDontHaveAn.setBounds(384, 210, 200, 14);
        getContentPane().add(lblDontHaveAn);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(451, 235, 112, 23);
        btnRegister.addActionListener(this);
        getContentPane().add(btnRegister);

        setSize(586, 363);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnRegister)) {
            if (registerButtonClickListener != null)
                registerButtonClickListener.run();
        } else {
            if (loginButtonClickListener != null)
                loginButtonClickListener.run();
        }
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
    public void setLoginButtonClickListener(Runnable listener) {
        this.loginButtonClickListener = listener;
    }

    @Override
    public void setRegisterButtonClickListener(Runnable listener) {
        this.registerButtonClickListener = listener;
    }
}
