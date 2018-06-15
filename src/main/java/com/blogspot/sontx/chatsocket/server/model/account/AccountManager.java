package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.LoginInfo;

import java.util.List;

/**
 * Manages accounts.
 */
public interface AccountManager {

    List<AccountInfo> getAllAccounts();

    AccountInfo findAccountByLoginInfo(LoginInfo loginInfo);

    void setPasswordHash(int accountId, String passwordHash);

    AccountInfo findAccountByUserName(String username);

    AccountInfo addAccount(String username, String passwordHash, String displayName);

    void updateDetail(AccountInfo accountInfo);
}
