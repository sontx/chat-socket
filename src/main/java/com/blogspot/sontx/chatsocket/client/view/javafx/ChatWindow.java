package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.ChatView;
import com.blogspot.sontx.chatsocket.client.view.javafx.custom.ChatMessageCell;
import com.blogspot.sontx.chatsocket.client.view.javafx.custom.ChatMessageInternal;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
class ChatWindow extends BaseJavaFxWindow implements ChatView {
    @FXML
    private TextArea messageTextArea;
    @FXML
    private GridPane messageBox;
    @FXML
    private ScrollPane scrollPane;

    @Setter
    private Callback<String> sendButtonClickListener;

    ChatWindow() {
        init(this, "chat-window.fxml");
    }

    @FXML
    private void onMessageTextAreaEnter() {
        if (sendButtonClickListener != null)
            sendButtonClickListener.call(messageTextArea.getText());
    }

    @Override
    public void appendFriendMessage(String message) {
        showChatMessage(new ChatMessageInternal(message, false));
    }

    private void showChatMessage(ChatMessageInternal chatMessageInternal) {
        try {
            ChatMessageCell chatMessageCell = new ChatMessageCell(chatMessageInternal);
            messageBox.add(chatMessageCell, 0, messageBox.getChildren().size());
            scrollPane.setVvalue(1.0);
        } catch (IOException e) {
            log.error("Error while create chat message cell", e);
        }
    }

    @Override
    public void appendMeMyMessage(String message) {
        showChatMessage(new ChatMessageInternal(message, true));
    }

    @Override
    public void clearInput() {
        messageTextArea.clear();
    }
}
