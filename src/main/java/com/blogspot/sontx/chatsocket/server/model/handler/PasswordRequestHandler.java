package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.Request;
import com.blogspot.sontx.chatsocket.lib.bean.Response;
import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

/**
 * Handles updating password request from the client.
 */
class PasswordRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    PasswordRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    Response handleWithAuthenticated(RequestReceivedEvent event) {
        Request request = event.getRequest();
        if (request.getExtra() != null && request.getExtra() instanceof UpdatePassword) {
            UpdatePassword updatePassword = (UpdatePassword) request.getExtra();
            if (Security.checkValidPassword(updatePassword.getOldPassword()) && Security.checkValidPassword(updatePassword.getNewPassword())) {
                String currentPassword = updatePassword.getOldPassword();
                String newPassword = updatePassword.getNewPassword();
                String currentPasswordHash = Security.getPasswordHash(currentPassword);
                String newPasswordHash = Security.getPasswordHash(newPassword);
                if (accountManager.setPasswordHash(event.getProfile().getId(), currentPasswordHash, newPasswordHash)) {
                    return okResponse(null, event.getRequest().getCode());
                }
            }
        }
        return failResponse("Invalid password.", event.getRequest().getCode());
    }
}
