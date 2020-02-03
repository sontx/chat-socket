package com.blogspot.sontx.chatsocket.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profile extends com.blogspot.sontx.chatsocket.lib.bean.Profile {
    private String username;

    public void update(com.blogspot.sontx.chatsocket.lib.bean.Profile profile) {
        if (profile != null) {
            super.setAccountId(profile.getAccountId());
            super.setDisplayName(profile.getDisplayName());
            super.setStatus(profile.getStatus());
            super.setState(profile.getState());
        } else {
            super.setAccountId(-1);
        }
    }
}


