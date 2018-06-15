package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;

/**
 * Manages received chat messages.
 */
public interface ChattingManager {
    void start();

    void stop();

    void processReceivedChatMessage(ChatMessage chatMessage);

    void processSentChatMessage(Response response);
}
