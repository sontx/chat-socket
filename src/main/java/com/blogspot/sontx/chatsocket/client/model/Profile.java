package com.blogspot.sontx.chatsocket.client.model;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profile extends AccountInfo {
    private String username;

    public void update(AccountInfo accountInfo) {
        if (accountInfo != null) {
            super.setAccountId(accountInfo.getAccountId());
            super.setDisplayName(accountInfo.getDisplayName());
            super.setStatus(accountInfo.getStatus());
            super.setState(accountInfo.getState());
        } else {
            super.setAccountId(-1);
        }
    }
}


