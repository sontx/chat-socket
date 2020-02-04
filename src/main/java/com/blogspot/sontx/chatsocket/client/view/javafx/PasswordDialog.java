package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
class PasswordDialog {
    @Setter
    private Callback<UpdatePassword> changePasswordButtonClickListener;
    private PasswordPane passwordPane;

    public void show() {
        if (initializeUI()) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Password");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(passwordPane);
        alert.getDialogPane()
                .lookupButton(ButtonType.OK)
                .addEventFilter(ActionEvent.ACTION, this::handleOK);
        alert.initStyle(StageStyle.UTILITY);
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
        if (changePasswordButtonClickListener != null) {
            UpdatePassword updatePassword = new UpdatePassword();
            updatePassword.setOldPassword(passwordPane.currentPasswordField.getText());
            updatePassword.setNewPassword(passwordPane.newPasswordField.getText());
            changePasswordButtonClickListener.call(updatePassword);
        }
    }

    public static class PasswordPane extends AnchorPane {
        @FXML
        private PasswordField currentPasswordField;
        @FXML
        private PasswordField newPasswordField;
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
