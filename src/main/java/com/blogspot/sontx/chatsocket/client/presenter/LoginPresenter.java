package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.ClientSettings;
import com.blogspot.sontx.chatsocket.client.event.LoggedEvent;
import com.blogspot.sontx.chatsocket.client.event.LoginEvent;
import com.blogspot.sontx.chatsocket.client.event.OpenRegisterEvent;
import com.blogspot.sontx.chatsocket.client.view.LoginView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import com.google.common.eventbus.Subscribe;

public class LoginPresenter extends AbstractPresenter<LoginView> {
    public LoginPresenter(LoginView loginView) {
        super(loginView);
    }

    @Override
    protected  void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setLoginButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid login info.", "Login", MessageType.Error);
            else
                login();
        });
        view.setRegisterButtonClickListener(this::register);
    }

    private boolean verifyInputs() {
        String username = view.getUsername();
        String password = view.getPassword();

        return Security.checkValidUsername(username) && Security.checkValidPassword(password);
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        post(new LoginEvent(username, password));

        ClientSettings settings = getSetting(ClientSettings.class);
        settings.setLoggedUserName(username);
    }

    @Subscribe
    public void onLogged(LoggedEvent event) {
        runOnUiThread(() -> {
            stop();
            view.closeWindow();
        });
    }

    private void register() {
        post(new OpenRegisterEvent());
    }

    public void show() {
        start();
        ClientSettings settings = getSetting(ClientSettings.class);
        view.setUserName(settings.getLoggedUserName());
        view.setTitle("Login");
        view.showWindow();
    }


}
