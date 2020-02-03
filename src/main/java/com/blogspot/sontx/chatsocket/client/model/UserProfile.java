package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends Profile {
    private String username;

    public void update(Profile profile) {
        if (profile != null) {
            super.setId(profile.getId());
            super.setDisplayName(profile.getDisplayName());
            super.setStatus(profile.getStatus());
            super.setState(profile.getState());
        } else {
            super.setId("");
        }
    }
}


