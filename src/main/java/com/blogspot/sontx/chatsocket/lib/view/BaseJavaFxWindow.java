package com.blogspot.sontx.chatsocket.lib.view;

import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import com.blogspot.sontx.chatsocket.lib.thread.Invoker;
import com.blogspot.sontx.chatsocket.lib.thread.JavaFxInvoker;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public abstract class BaseJavaFxWindow implements BaseView {
    @Getter
    @Setter
    private Stage stage;
    private Invoker invoker = new JavaFxInvoker();

    private boolean isMainWindow;

    protected void init(BaseJavaFxWindow controller, String layoutName) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LayoutsResource.getInstance().getResource(layoutName));
        fxmlLoader.setController(controller);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            controller.setStage(stage);
        } catch (IOException e) {
            log.error("Error while create MainWindow", e);
        }
    }

    private void centerToScreen() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @Override
    public void showWindow() {
        invoker.invokeLater(() -> {
            if (stage != null) {
                stage.show();
                centerToScreen();
            }
        });
    }

    @Override
    public void closeWindow() {
        invoker.invokeLater(() -> {
            if (stage != null)
                stage.close();
        });
    }

    @Override
    public void setOnClosingListener(Runnable listener) {
        invoker.invokeLater(() -> {
            if (stage == null) return;
            stage.setOnCloseRequest(event -> {
                if (listener != null)
                    listener.run();
                if (isMainWindow)
                    Platform.exit();
            });
        });
    }

    @Override
    public void setTitle(String title) {
        invoker.invokeLater(() -> {
            if (stage != null)
                stage.setTitle(title);
        });
    }

    @Override
    public void setMainWindow() {
        isMainWindow = true;
    }
}
