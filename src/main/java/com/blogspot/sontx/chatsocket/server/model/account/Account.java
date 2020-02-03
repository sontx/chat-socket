package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import lombok.Data;

@Data
class Account {
    private int id;
    private String username;
    private String passwordHash;
    private Profile profile;
}
