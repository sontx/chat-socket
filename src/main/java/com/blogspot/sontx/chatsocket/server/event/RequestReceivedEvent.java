package com.blogspot.sontx.chatsocket.server.event;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.Request;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Occurs when received a request from a client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestReceivedEvent {
    private Request request;
    private AccountInfo accountInfo;
    private Worker sender;
}
