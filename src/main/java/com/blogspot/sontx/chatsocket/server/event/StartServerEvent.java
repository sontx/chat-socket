package com.blogspot.sontx.chatsocket.server.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Starts server command.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartServerEvent {
    private String ip;
    private int port;
}
