package com.blogspot.sontx.chatsocket.server.event;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import lombok.Data;

/**
 * Finds a running {@link Worker} by an {@link AccountInfo}.
 */
@Data
public class LookupWorkerEvent {
    private AccountInfo matchedAccount;
    private Worker matchedWorker;
}
