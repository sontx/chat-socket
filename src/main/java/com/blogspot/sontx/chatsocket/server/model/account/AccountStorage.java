package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Reads/writes accounts data from/to storage.
 */
public interface AccountStorage {
    Optional<Account> findById(int id);

    Optional<Account> findByUserName(String username);

    Account add(Account account);

    List<Account> findAll();

    void updateDetail(int accountId, Profile profile);

    void updatePasswordHash(int accountId, String passwordHash);
}
