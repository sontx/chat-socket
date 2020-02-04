package com.blogspot.sontx.chatsocket.client.event;

import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import lombok.Data;

@Data
public class UpdatePasswordEvent {
    private UpdatePassword updatePassword;
}
