package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.LoggedEvent;
import com.blogspot.sontx.chatsocket.client.event.LoginEvent;
import com.blogspot.sontx.chatsocket.client.event.OpenRegisterEvent;
import com.blogspot.sontx.chatsocket.client.view.LoginView;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginPresenter extends AbstractService implements Presenter {
    private final LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        loginView.setLoginButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid login info.", "Login", MessageType.Error);
            else
                login();
        });
        loginView.setRegisterButtonClickListener(this::register);
    }

    private boolean verifyInputs() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        return Security.checkValidUsername(username) && Security.checkValidPassword(password);
    }

    private void login() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        post(new LoginEvent(username, password));
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onLogged(LoggedEvent event) {
        stop();
        loginView.closeWindow();
    }

    private void register() {
        post(new OpenRegisterEvent());
    }

    public void show() {
        start();
        loginView.setTitle("Login");
        loginView.showWindow();
    }


}
