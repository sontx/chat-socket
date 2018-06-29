package com.blogspot.sontx.chatsocket.lib.service.event;

import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowMessageBoxEvent {
    private String message;
    private String caption;
    private MessageType type;
}
