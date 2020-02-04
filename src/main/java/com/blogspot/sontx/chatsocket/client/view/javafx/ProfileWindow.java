package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.model.UserProfile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import com.blogspot.sontx.chatsocket.lib.view.AbstractJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import lombok.Setter;

class ProfileWindow extends AbstractJavaFxWindow implements ProfileView {
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField displayNameTextField;
    @FXML
    private TextField statusTextField;

    @Setter
    private Callback<String> changeDisplayNameButtonClickListener;
    @Setter
    private Callback<String> changeStatusButtonClickListener;
    @Setter
    private Callback<UpdatePassword> changePasswordButtonClickListener;

    ProfileWindow() {
        init(this, "profile-window.fxml", false);
        getStage().initStyle(StageStyle.UTILITY);
    }

    @FXML
    private void onChangePasswordButtonClick() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.setChangePasswordButtonClickListener(password -> {
            if (changePasswordButtonClickListener != null)
                changePasswordButtonClickListener.call(password);
        });
        passwordDialog.show();
    }

    @FXML
    private void onChangeDisplayNameButtonClick() {
        if (changeDisplayNameButtonClickListener != null)
            changeDisplayNameButtonClickListener.call(displayNameTextField.getText());
    }

    @FXML
    private void onChangeStatusButtonClick() {
        if (changeStatusButtonClickListener != null)
            changeStatusButtonClickListener.call(statusTextField.getText());
    }

    @FXML
    private void onCloseButtonClick() {
        closeWindow();
    }

    @Override
    public void setProfile(UserProfile userProfile) {
        displayNameTextField.setText(userProfile.getDisplayName());
        statusTextField.setText(userProfile.getStatus());
        usernameLabel.setText(userProfile.getUsername());
    }
}
