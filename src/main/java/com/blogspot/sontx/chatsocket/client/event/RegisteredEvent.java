package com.blogspot.sontx.chatsocket.client.event;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredEvent {
    private Profile registeredAccount;
}
