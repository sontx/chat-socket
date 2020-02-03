package com.blogspot.sontx.chatsocket.server.event;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import lombok.Data;

/**
 * Finds a running {@link Worker} by an {@link Profile}.
 */
@Data
public class LookupWorkerEvent {
    private Profile matchedAccount;
    private Worker matchedWorker;
}
