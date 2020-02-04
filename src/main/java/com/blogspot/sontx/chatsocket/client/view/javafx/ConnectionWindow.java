package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

class ConnectionWindow extends BaseJavaFxWindow implements ConnectionView {
    @Setter
    private Runnable connectButtonClickListener;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField portTextField;

    ConnectionWindow() {
        init(this, "connection-window.fxml", false);
    }

    @FXML
    private void onConnectButtonClick() {
        if (connectButtonClickListener != null) {
            connectButtonClickListener.run();
        }
    }

    @FXML
    private void onLogoClick() {
        try {
            Desktop.getDesktop().browse(URI.create(AppConfig.getDefault().getHomePageUrl()));
        } catch (IOException ignored) {
        }
    }

    @Override
    public String getServerIp() {
        return addressTextField.getText();
    }

    @Override
    public String getServerPort() {
        return portTextField.getText();
    }

    @Override
    public void setServerIp(String serverIp) {
        addressTextField.setText(serverIp);
    }

    @Override
    public void setServerPort(int serverPort) {
        portTextField.setText(Integer.toString(serverPort));
    }
}
