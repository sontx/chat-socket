package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Optional;

@Log4j
class PasswordDialog {
    @Setter
    private Callback<String> changePasswordButtonClickListener;
    private PasswordPane passwordPane;

    public void show() {
        if (initializeUI()) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(passwordPane);
        alert.getDialogPane()
                .lookupButton(ButtonType.OK)
                .addEventFilter(ActionEvent.ACTION, this::handleOK);
        alert.showAndWait();
    }

    private boolean initializeUI() {
        try {
            passwordPane = new PasswordPane();
        } catch (IOException e) {
            log.error("Error while show password dialog", e);
            return true;
        }
        return false;
    }

    private void handleOK(ActionEvent event) {
        String password = passwordPane.passwordField.getText();
        if (!valid(password)) {
            showError("Invalid password");
            event.consume();
            return;
        }

        String repassword = passwordPane.rePasswordField.getText();
        if (!match(password, repassword)) {
            showError("Password does not match");
            event.consume();
            return;
        }

        if (changePasswordButtonClickListener != null) {
            changePasswordButtonClickListener.call(password);
        }
    }

    private boolean match(String password, String rePassword) {
        return password.equals(rePassword);
    }

    private boolean valid(String password) {
        return Security.checkValidPassword(password);
    }

    private void showError(String message) {
        passwordPane.errorLabel.setText(message);
    }

    public static class PasswordPane extends AnchorPane {
        @FXML
        private PasswordField passwordField;
        @FXML
        private PasswordField rePasswordField;
        @FXML
        private Label errorLabel;

        PasswordPane() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(LayoutsResource.getInstance().getResource("password-dialog.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        }
    }
}
