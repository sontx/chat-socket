package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bean.LoginInfo;

import java.util.List;

/**
 * Manages accounts.
 */
public interface AccountManager {

    List<Profile> getAllAccounts();

    Profile findAccountByLoginInfo(LoginInfo loginInfo);

    boolean setPasswordHash(String accountId, String currentPasswordHash, String newPasswordHash);

    Profile findAccountByUserName(String username);

    Profile addAccount(String username, String passwordHash, String displayName);

    void updateDetail(Profile profile);
}
