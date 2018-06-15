package com.blogspot.sontx.chatsocket.server.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shutdown {@link com.blogspot.sontx.chatsocket.server.model.Server} command.
 * All {@link com.blogspot.sontx.chatsocket.server.model.Server} that have corresponding sessionId
 * should shutdown.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShutdownServerEvent {
    public static final int ALL = -1;

    private int sessionId;
}
