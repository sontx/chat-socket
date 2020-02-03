package com.blogspot.sontx.chatsocket.server.model.account;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Reads/writes accounts data from/to storage.
 */
public interface AccountStorage {
    Optional<Account> findById(String id);

    Optional<Account> findByUserName(String username);

    Account add(Account account);

    List<Account> findAll();

    void updateDetail(String accountId, Profile profile);

    void updatePasswordHash(String accountId, String passwordHash);
}
