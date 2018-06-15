package com.blogspot.sontx.chatsocket.client.view.custom;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import lombok.Getter;

import javax.swing.*;

public class FriendEntry {
    @Getter
    private final AccountInfo accountInfo;

    public FriendEntry(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public boolean contains(AccountInfo accountInfo) {
        return accountInfo != null && accountInfo.getAccountId() == this.accountInfo.getAccountId();
    }

    public String getDisplayName() {
        return accountInfo.getDisplayName();
    }

    ImageIcon getImage() {
        String imageName = accountInfo.isOnline() ? "online.png" : "offline.png";
        return new ImageIcon(ImagesResource.getInstance().getImageByName(imageName));
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    String getStatus() {
        return accountInfo.getStatus();
    }
}
