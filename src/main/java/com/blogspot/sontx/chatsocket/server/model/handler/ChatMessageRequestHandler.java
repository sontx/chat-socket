package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.*;
import com.blogspot.sontx.chatsocket.server.event.ForwardChatMessageEvent;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;

/**
 * Handles sending chat message request from the client, the chat message
 * will be forwarded to a corresponding {@link com.blogspot.sontx.chatsocket.server.model.Worker}.
 */
class ChatMessageRequestHandler extends AbstractRequestHandler {
    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) throws Exception {
        Request request = event.getRequest();
        if (request.getExtra() instanceof ChatMessage)
            return forwardChatMessage((ChatMessage) request.getExtra(), event.getProfile());
        return failResponse("Extra must be chat message.", event.getRequest().getCode());
    }

    private Response forwardChatMessage(ChatMessage chatMessage, Profile profile) {
        int receiverId = chatMessage.getWhoId();
        int senderId = profile.getAccountId();

        ChatMessage forwardMessage = new ChatMessage(senderId, chatMessage.getContent());

        ForwardChatMessageEvent event = new ForwardChatMessageEvent(forwardMessage, receiverId, null);
        post(event);

        Response result = event.getResponseResult();
        return result != null ? result : failResponse("Friend was offline", RequestCode.ChatMessage);
    }
}
