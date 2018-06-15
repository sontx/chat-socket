package com.blogspot.sontx.chatsocket.server.event;

import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat message that already received from a client will
 * be forwarded to a {@link com.blogspot.sontx.chatsocket.server.model.Worker} to send to the receiver.
 * The {@link com.blogspot.sontx.chatsocket.server.model.Worker} should set the sent result to responseResult.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForwardChatMessageEvent {
    private ChatMessage forwardMessage;
    private int receiverId;
    private Response responseResult;
}
