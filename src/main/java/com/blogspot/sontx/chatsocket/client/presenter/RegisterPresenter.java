package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.RegisterEvent;
import com.blogspot.sontx.chatsocket.client.event.RegisteredEvent;
import com.blogspot.sontx.chatsocket.client.view.RegisterView;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RegisterPresenter {
    private final RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        registerView.setRegisterButtonClickListener(() -> {
            if (!verifyInputs())
                registerView.showMessageBox("Invalid register info.");
            else
                register();
        });
    }

    private boolean verifyInputs() {
        String username = registerView.getUsername();
        String password = registerView.getPassword();
        String displayName = registerView.getDisplayName();

        return Security.checkValidUsername(username)
                && Security.checkValidPassword(password)
                && Security.checkValidDisplayName(displayName);
    }

    private void register() {
        String username = registerView.getUsername();
        String password = registerView.getPassword();
        String displayName = registerView.getDisplayName();

        EventBus.getDefault().post(new RegisterEvent(username, password, displayName));
    }

    @Subscribe
    public void onRegistered(RegisteredEvent event) {
        EventBus.getDefault().unregister(this);
        registerView.closeWindow();
    }

    public void show() {
        EventBus.getDefault().register(this);
        registerView.setTitle("Register");
        registerView.showWindow();
    }
}
