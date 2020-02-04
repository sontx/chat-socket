package com.blogspot.sontx.chatsocket.lib.service.message;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.net.URL;

public class JavaFxMessageBox implements MessageBox {
    @Override
    public void show(String caption, String text, MessageType type) {
        Alert alert = new Alert(getAlertType(type));
        URL imageUrl = getUrlByType(type);
        alert.setGraphic(new ImageView(imageUrl != null ? imageUrl.toString() : ""));
        alert.setContentText(getHeaderText(type));
        alert.setTitle(caption);
        alert.setHeaderText(text);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    private Alert.AlertType getAlertType(MessageType type) {
        switch (type) {
            case Error:
                return Alert.AlertType.ERROR;
            case Info:
                return Alert.AlertType.INFORMATION;
        }
        return Alert.AlertType.NONE;
    }

    private URL getUrlByType(MessageType type) {
        switch (type) {
            case Error:
                return ImagesResource.getInstance().getImageAsUrl("msgbox-error.png");
            case Info:
                return ImagesResource.getInstance().getImageAsUrl("msgbox-info.png");
        }
        return null;
    }

    private String getHeaderText(MessageType type) {
        switch (type) {
            case Error:
                return "Something went wrong!";
            case Info:
                return "Look good!";
        }
        return "";
    }
}
