package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.bean.LoginInfo;
import com.blogspot.sontx.chatsocket.lib.utils.Security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountManagerImpl implements AccountManager {
    private final AccountStorage accountStorage;

    public AccountManagerImpl(AccountStorage accountStorage) {
        this.accountStorage = accountStorage;
    }

    @Override
    public List<AccountInfo> getAllAccounts() {
        List<Account> accounts = accountStorage.findAll();
        return accounts.stream().map(Account::getDetail).collect(Collectors.toList());
    }

    @Override
    public AccountInfo findAccountByLoginInfo(LoginInfo loginInfo) {
        Optional<Account> account = accountStorage.findByUserName(loginInfo.getUsername());
        String passwordHash = Security.getPasswordHash(loginInfo.getPassword());
        if (account.isPresent() && account.get().getPasswordHash().equals(passwordHash))
            return account.get().getDetail();
        return null;
    }

    @Override
    public void setPasswordHash(int accountId, String passwordHash) {
        Optional<Account> account = accountStorage.findById(accountId);
        account.ifPresent(presentAccount -> presentAccount.setPasswordHash(passwordHash));
        accountStorage.updatePasswordHash(accountId, passwordHash);
    }

    @Override
    public AccountInfo findAccountByUserName(String username) {
        Optional<Account> account = accountStorage.findByUserName(username);
        return account.map(Account::getDetail).orElse(null);
    }

    @Override
    public AccountInfo addAccount(String username, String passwordHash, String displayName) {
        Account account = new Account();
        account.setUsername(username);
        account.setPasswordHash(passwordHash);
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setDisplayName(displayName);
        accountInfo.setState(AccountInfo.STATE_OFFLINE);
        account.setDetail(accountInfo);
        return accountStorage.add(account).getDetail();
    }

    @Override
    public void updateDetail(AccountInfo accountInfo) {
        accountStorage.updateDetail(accountInfo.getAccountId(), accountInfo);
    }
}
