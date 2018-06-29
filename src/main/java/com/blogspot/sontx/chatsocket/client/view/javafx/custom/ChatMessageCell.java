package com.blogspot.sontx.chatsocket.client.view.javafx.custom;

import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ChatMessageCell extends Label {
    public ChatMessageCell(ChatMessageInternal chatMessage) throws IOException {
        String layoutName = chatMessage.isMe() ? "chat-message-mine.fxml" : "chat-message-friend.fxml";
        FXMLLoader loader = new FXMLLoader(LayoutsResource.getInstance().getResource(layoutName));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
        setText(chatMessage.getContent());
        GridPane.setHalignment(this, chatMessage.isMe() ? HPos.RIGHT : HPos.LEFT);
    }
}
