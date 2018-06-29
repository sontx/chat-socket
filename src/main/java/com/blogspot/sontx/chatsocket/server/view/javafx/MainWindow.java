package com.blogspot.sontx.chatsocket.server.view.javafx;

import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import com.blogspot.sontx.chatsocket.server.view.LogView;
import com.blogspot.sontx.chatsocket.server.view.MainView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

@Log4j
class MainWindow extends BaseJavaFxWindow implements MainView, LogView {
    @FXML
    private TextArea logTextArea;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Button startButton;

    @Setter
    private Runnable startButtonListener;

    MainWindow() {
        init(this, "main-window.fxml");
    }

    @FXML
    private void onStartClick() {
        if (startButtonListener != null)
            startButtonListener.run();
    }

    @FXML
    private void onAddressBrowseClick() {
        AddressDialog addressDialog = new AddressDialog();
        addressDialog.setOnSelectedAddress(address -> ipTextField.setText(address));
        addressDialog.show();
    }

    @Override
    public synchronized void appendLog(String message) {
        if (StringUtils.isEmpty(logTextArea.getText()))
            logTextArea.setText(message);
        else
            logTextArea.appendText(System.lineSeparator() + message);
    }

    @Override
    public void clearLog() {
        logTextArea.clear();
    }

    @Override
    public String getIp() {
        return ipTextField.getText();
    }

    @Override
    public String getPort() {
        return portTextField.getText();
    }

    @Override
    public void setStartButtonText(String text) {
        startButton.setText(text);
    }
}
