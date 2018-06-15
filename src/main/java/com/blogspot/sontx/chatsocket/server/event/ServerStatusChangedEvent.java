package com.blogspot.sontx.chatsocket.server.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Occurs when server's status changed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerStatusChangedEvent {
    private boolean running;
}
