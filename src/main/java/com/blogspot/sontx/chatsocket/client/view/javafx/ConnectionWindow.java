package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

class ConnectionWindow extends BaseJavaFxWindow implements ConnectionView {
    @Setter
    private Runnable connectButtonClickListener;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField portTextField;

    ConnectionWindow() {
        init(this, "connection-window.fxml");
    }

    @FXML
    private void onConnectButtonClick() {
        if (connectButtonClickListener != null) {
            connectButtonClickListener.run();
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
}
