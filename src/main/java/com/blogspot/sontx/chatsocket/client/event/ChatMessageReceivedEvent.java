package com.blogspot.sontx.chatsocket.client.event;

import com.blogspot.sontx.chatsocket.lib.bean.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReceivedEvent {
    private ChatMessage chatMessage;
}
