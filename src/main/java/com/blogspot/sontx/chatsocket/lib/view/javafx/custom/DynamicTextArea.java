package com.blogspot.sontx.chatsocket.lib.view.javafx.custom;

import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.List;

@Log4j
public class DynamicTextArea extends TextArea {
    private static final double MAGIC_NUMBER = 10;

    private Text textHolder = new Text();
    private double oldHeight = 0;
    private ObjectProperty<EventHandler<KeyEvent>> propertyOnAction = new SimpleObjectProperty<>();

    public DynamicTextArea() {
        loadLayout();
        registerLayoutLogic();
    }

    private void loadLayout() {
        FXMLLoader loader = new FXMLLoader(LayoutsResource.getInstance().getResource("dynamic-text-area.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            log.error("Error while create DynamicTextArea", e);
        }
    }

    private void registerLayoutLogic() {
        setPrefHeight(0);
        setWrapText(true);
        textHolder.setFont(getFont());

        layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (textHolder.getWrappingWidth() != newValue.getWidth()) {
                textHolder.setWrappingWidth(newValue.getWidth() - MAGIC_NUMBER);
                if (textHolder.textProperty().isBound()) {
                    textHolder.textProperty().unbind();
                    textHolder.setText("");
                }
                textHolder.textProperty().bind(textProperty());
            }
        });

        textHolder.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> updateHeightIfNecessary(newValue.getHeight()));
    }

    private void updateHeightIfNecessary(double newHeight) {
        if (oldHeight != newHeight) {
            oldHeight = newHeight;
            setPrefHeight(newHeight + MAGIC_NUMBER);
        }
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.isShiftDown()) {
                appendText(System.lineSeparator());
            } else {
                onActionProperty().get().handle(event);
            }
        }
    }

    public final ObjectProperty<EventHandler<KeyEvent>> onActionProperty() {
        return propertyOnAction;
    }

    public final EventHandler<KeyEvent> getOnAction() {
        return propertyOnAction.get();

    }

    public final void setOnAction(EventHandler<KeyEvent> handler) {
        propertyOnAction.set(handler);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
