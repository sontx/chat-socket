package com.blogspot.sontx.chatsocket.client.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEvent {
    private String username;
    private String password;
    private String displayName;
}
