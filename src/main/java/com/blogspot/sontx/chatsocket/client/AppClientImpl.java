package com.blogspot.sontx.chatsocket.client;

import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.model.*;
import com.blogspot.sontx.chatsocket.client.model.handler.ResponseHandlerFactory;
import com.blogspot.sontx.chatsocket.client.presenter.*;
import com.blogspot.sontx.chatsocket.client.view.*;
import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import com.blogspot.sontx.chatsocket.lib.view.WindowUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Default implementation of {@link Client} that uses:
 * <pre>
 * Connection: tcp socket.
 * Serialization: basic java {@link java.io.Serializable} feature.
 * Components communication: eventbus.
 * </pre>
 */
public class AppClientImpl implements AppClient {
    private final Profile userProfile = new Profile();
    private Client client;
    private ChattingManager chattingManager = new ChattingManagerImpl();

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        showUI();
    }

    private void showUI() {
        WindowUtils.setSystemLookAndFeel();
        openConnectionWindow();
    }

    private void openConnectionWindow() {
        ConnectionPresenter connectionPresenter = new ConnectionPresenter(new ConnectionWindow());
        connectionPresenter.show();
    }

    @Subscribe
    public void onConnectToServer(ConnectToServerEvent event) {
        StreamUtils.tryCloseStream(client);
        chattingManager.start();
        client = new SocketClient(
                event.getServerIp(),
                event.getServerPort(),
                new ResponseHandlerFactory(chattingManager));
        client.start();
    }

    @Subscribe
    public void onConnectedToServer(ConnectedToServerEvent event) {
        openLoginWindow();
    }

    private void openLoginWindow() {
        LoginPresenter loginPresenter = new LoginPresenter(new LoginWindow());
        loginPresenter.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogged(LoggedEvent event) {
        userProfile.update(event.getLoggedAccount());
        openMainWindow();
    }

    private void openMainWindow() {
        FriendListPresenter friendListPresenter = new FriendListPresenter(new FriendListWindow());
        friendListPresenter.setMyAccountInfo(userProfile);
        friendListPresenter.show();
    }

    @Subscribe
    public void onOpenChat(OpenChatEvent event) {
        ChatPresenter chatPresenter = new ChatPresenter(new ChatWindow(), event.getChatWith());
        chatPresenter.show();
    }

    @Subscribe
    public void onOpenMyProfile(OpenMyProfileEvent event) {
        ProfilePresenter profilePresenter = new ProfilePresenter(new ProfileWindow(), userProfile);
        profilePresenter.show();
    }

    @Subscribe
    public void onLoggin(LoginEvent event) {
        userProfile.setUsername(event.getUsername());
    }

    @Subscribe
    public void onOpenRegister(OpenRegisterEvent event) {
        RegisterPresenter registerPresenter = new RegisterPresenter(new RegisterWindow());
        registerPresenter.show();
    }

    @Subscribe
    public void onRegistered(RegisteredEvent event) {
        MessageBox.showInUIThread(
                null,
                "Registered! use your new account to login.",
                MessageBox.MESSAGE_INFO);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientShutdown(ClientShutdownEvent event) {
        cleanResources();
        if (event.getReason() != null) {
            MessageBox.showInUIThread(null, event.getReason().getMessage(), MessageBox.MESSAGE_ERROR);
        }
    }

    @Subscribe
    public void onAppShutdown(AppShutdownEvent event) {
        cleanResources();
    }

    private void cleanResources() {
        StreamUtils.tryCloseStream(client);
        chattingManager.stop();
    }
}
