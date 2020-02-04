package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
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
    public List<Profile> getAllAccounts() {
        List<Account> accounts = accountStorage.findAll();
        return accounts.stream().map(Account::getProfile).collect(Collectors.toList());
    }

    @Override
    public Profile findAccountByLoginInfo(LoginInfo loginInfo) {
        Optional<Account> account = accountStorage.findByUserName(loginInfo.getUsername());
        String passwordHash = Security.getPasswordHash(loginInfo.getPassword());
        if (account.isPresent() && account.get().getPasswordHash().equals(passwordHash))
            return account.get().getProfile();
        return null;
    }

    @Override
    public boolean setPasswordHash(String accountId, String currentPasswordHash, String newPasswordHash) {
        Optional<Account> account = accountStorage.findById(accountId);
        if (account.isPresent()) {
            Account presentAccount = account.get();
            if (presentAccount.getPasswordHash().equals(currentPasswordHash)) {
                accountStorage.updatePasswordHash(accountId, newPasswordHash);
                return true;
            }
        }
        return false;
    }

    @Override
    public Profile findAccountByUserName(String username) {
        Optional<Account> account = accountStorage.findByUserName(username);
        return account.map(Account::getProfile).orElse(null);
    }

    @Override
    public Profile addAccount(String username, String passwordHash, String displayName) {
        Account account = new Account();
        account.setUsername(username);
        account.setPasswordHash(passwordHash);
        Profile profile = new Profile();
        profile.setDisplayName(displayName);
        profile.setState(Profile.STATE_OFFLINE);
        account.setProfile(profile);
        return accountStorage.add(account).getProfile();
    }

    @Override
    public void updateDetail(Profile profile) {
        accountStorage.updateDetail(profile.getId(), profile);
    }
}
