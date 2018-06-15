package com.blogspot.sontx.chatsocket.client.model.handler;

import com.blogspot.sontx.chatsocket.client.model.ChattingManager;
import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bo.ObjectTransmission;

public class ChatMessageResponseHandler extends AbstractResponseHandler {
    private final ChattingManager chattingManager;

    ChatMessageResponseHandler(ChattingManager chattingManager) {
        this.chattingManager = chattingManager;
    }

    @Override
    public void handle(ObjectTransmission transmission, Response response) throws Exception {
        if (response.getExtra() instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) response.getExtra();
            chattingManager.processReceivedChatMessage(chatMessage);
        } else {
            chattingManager.processSentChatMessage(response);
        }
    }
}
