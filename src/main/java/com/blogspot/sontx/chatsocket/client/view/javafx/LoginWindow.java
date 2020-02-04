package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.LoginView;
import com.blogspot.sontx.chatsocket.lib.view.AbstractJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

class LoginWindow extends AbstractJavaFxWindow implements LoginView {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @Setter
    private Runnable loginButtonClickListener;
    @Setter
    private Runnable registerButtonClickListener;

    LoginWindow() {
        init(this, "login-window.fxml", false);
    }

    @FXML
    private void onLoginButtonClick() {
        if (loginButtonClickListener != null)
            loginButtonClickListener.run();
    }

    @FXML
    private void onRegisterButtonClick() {
        if (registerButtonClickListener != null)
            registerButtonClickListener.run();
    }

    @Override
    public String getUsername() {
        return usernameTextField.getText();
    }

    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public void setUserName(String loggedUserName) {
        usernameTextField.setText(loggedUserName);
    }
}
