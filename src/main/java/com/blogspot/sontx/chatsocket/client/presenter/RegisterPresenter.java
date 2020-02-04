package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.RegisterEvent;
import com.blogspot.sontx.chatsocket.client.event.RegisteredEvent;
import com.blogspot.sontx.chatsocket.client.view.RegisterView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.google.common.eventbus.Subscribe;

public class RegisterPresenter extends AbstractPresenter<RegisterView> {
    public RegisterPresenter(RegisterView registerView) {
        super(registerView);
    }

    @Override
    protected  void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setRegisterButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid register info.", "Registration", MessageType.Error);
            else
                register();
        });
    }

    private boolean verifyInputs() {
        String username = view.getUsername();
        String password = view.getPassword();
        String displayName = view.getDisplayName();

        return Security.checkValidUsername(username)
                && Security.checkValidPassword(password)
                && Security.checkValidDisplayName(displayName);
    }

    private void register() {
        String username = view.getUsername();
        String password = view.getPassword();
        String displayName = view.getDisplayName();

        post(new RegisterEvent(username, password, displayName));
    }

    @Subscribe
    public void onRegistered(RegisteredEvent event) {
        runOnUiThread(() -> {
            stop();
            view.closeWindow();
        });
    }

    public void show() {
        start();
        view.setTitle("Register");
        view.showWindow();
    }
}
