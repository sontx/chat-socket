package com.blogspot.sontx.chatsocket.server.model.handler;

import com.blogspot.sontx.chatsocket.lib.bean.*;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.blogspot.sontx.chatsocket.server.event.RequestReceivedEvent;
import com.blogspot.sontx.chatsocket.server.model.Worker;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;

/**
 * Handles register new account request from the client.
 */
class RegisterRequestHandler extends AbstractRequestHandler {
    private final AccountManager accountManager;

    RegisterRequestHandler(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public void handle(RequestReceivedEvent event) throws Exception {
        Request request = event.getRequest();
        Worker sender = event.getSender();
        if (request.getExtra() instanceof RegisterInfo) {
            Response result = register((RegisterInfo) request.getExtra());
            if (result.getCode() == ResponseCode.OK) {
                Profile profile = (Profile) result.getExtra();
                broadcastAccountInfoChanged(profile);
            }
            sender.response(result);
        } else {
            sender.response(failResponse("Extra must be register info", event.getRequest().getCode()));
        }
    }

    private Response register(RegisterInfo registerInfo) {
        if (!Security.checkValidUsername(registerInfo.getUsername())
                || !Security.checkValidPassword(registerInfo.getPassword())
                || !Security.checkValidDisplayName(registerInfo.getDisplayName()))
            return failResponse("Invalid register info.", RequestCode.Register);

        String username = registerInfo.getUsername().trim();
        Profile profile = accountManager.findAccountByUserName(username);
        if (profile != null)
            return failResponse("Username already exists", RequestCode.Register);

        String passwordHash = Security.getPasswordHash(registerInfo.getPassword());
        String displayName = registerInfo.getDisplayName().trim();
        profile = accountManager.addAccount(username, passwordHash, displayName);
        return okResponse(profile, RequestCode.Register);
    }
}
