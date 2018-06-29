package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.RegisterView;
import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

class RegisterWindow extends BaseJavaFxWindow implements RegisterView {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField displayNameTextField;
    @FXML
    private PasswordField passwordField;
    @Setter
    private Runnable registerButtonClickListener;

    RegisterWindow() {
        init(this, "register-window.fxml");
    }

    @FXML
    private void onOKButtonClick() {
        if (registerButtonClickListener != null)
            registerButtonClickListener.run();
    }

    @FXML
    private void onCancelButtonClick() {
        closeWindow();
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
    public String getDisplayName() {
        return displayNameTextField.getText();
    }
}
