package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.RegisterEvent;
import com.blogspot.sontx.chatsocket.client.event.RegisteredEvent;
import com.blogspot.sontx.chatsocket.client.view.RegisterView;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterPresenter extends AbstractService implements Presenter {
    private final RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        registerView.setRegisterButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid register info.", "Registration", MessageType.Error);
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

        post(new RegisterEvent(username, password, displayName));
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onRegistered(RegisteredEvent event) {
        stop();
        registerView.closeWindow();
    }

    public void show() {
        start();
        registerView.setTitle("Register");
        registerView.showWindow();
    }
}
