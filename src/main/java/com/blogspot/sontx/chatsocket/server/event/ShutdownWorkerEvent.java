package com.blogspot.sontx.chatsocket.server.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shutdown {@link com.blogspot.sontx.chatsocket.server.model.Worker} command.
 * All {@link com.blogspot.sontx.chatsocket.server.model.Worker} that have the corresponding serverSession
 * should shutdown.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShutdownWorkerEvent {
    private int serverSession;
}
