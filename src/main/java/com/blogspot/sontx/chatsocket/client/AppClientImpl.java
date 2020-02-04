package com.blogspot.sontx.chatsocket.client;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.model.*;
import com.blogspot.sontx.chatsocket.client.model.handler.ResponseHandlerFactory;
import com.blogspot.sontx.chatsocket.client.presenter.*;
import com.blogspot.sontx.chatsocket.client.view.*;
import com.blogspot.sontx.chatsocket.lib.Component;
import com.blogspot.sontx.chatsocket.lib.Presenter;
import com.blogspot.sontx.chatsocket.lib.event.AppShutdownEvent;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.lib.service.event.ShowMessageBoxEvent;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.google.common.eventbus.Subscribe;

/**
 * Default implementation of {@link AppClient} that uses:
 * <pre>
 * Connection: tcp socket.
 * Serialization: basic java {@link java.io.Serializable} feature.
 * Components communication: guava.
 * </pre>
 */
public class AppClientImpl extends Component implements AppClient {
    private final UserProfile userProfile = new UserProfile();
    private Client client;
    private ChattingManager chattingManager;

    @Override
    public void start(Platform platform) {
        setPlatform(platform);
        platform.attach(this);
        initializeChattingManager(platform);
        showUI();
    }

    private void initializeChattingManager(Platform platform) {
        chattingManager = new ChattingManagerImpl();
        ((ChattingManagerImpl) chattingManager).setPlatform(platform);
    }

    private void showUI() {
        openConnectionWindow();
    }

    private void openConnectionWindow() {
        startPresenter(new ConnectionPresenter(create(ConnectionView.class)));
    }

    private void startPresenter(Presenter presenter) {
        presenter.setPlatform(getPlatform());
        presenter.show();
    }

    private <T> T create(Class<T> viewType) {
        return getPlatform().getThreadInvoker().invokeWithResult(() -> getPlatform().getViewFactory().create(viewType));
    }

    @Subscribe
    public void onConnectToServer(ConnectToServerEvent event) {
        StreamUtils.tryCloseStream(client);
        chattingManager.start();
        client = new SocketClient(
                event.getServerIp(),
                event.getServerPort(),
                new ResponseHandlerFactory(chattingManager, getPlatform()));
        ((SocketClient) client).setPlatform(getPlatform());
        client.start();
    }

    @Subscribe
    public void onConnectedToServer(ConnectedToServerEvent event) {
        runOnUiThread(this::openLoginWindow);
    }

    private void openLoginWindow() {
        startPresenter(new LoginPresenter(create(LoginView.class)));
    }

    @Subscribe
    public void onLogged(LoggedEvent event) {
        runOnUiThread(() -> {
            userProfile.update(event.getLoggedAccount());
            openMainWindow();
        }, false);
    }

    private void openMainWindow() {
        FriendListPresenter friendListPresenter = new FriendListPresenter(create(FriendListView.class));
        startPresenter(friendListPresenter);
        friendListPresenter.setMyProfile(userProfile);
    }

    @Subscribe
    public void onOpenChat(OpenChatEvent event) {
        runOnUiThread(() -> startPresenter(new ChatPresenter(create(ChatView.class), event.getChatWith())), false);
    }

    @Subscribe
    public void onOpenMyProfile(OpenMyProfileEvent event) {
        runOnUiThread(() -> startPresenter(new ProfilePresenter(create(ProfileView.class), userProfile)));
    }

    @Subscribe
    public void onLoggin(LoginEvent event) {
        userProfile.setUsername(event.getUsername());
    }

    @Subscribe
    public void onOpenRegister(OpenRegisterEvent event) {
        runOnUiThread(() -> startPresenter(new RegisterPresenter(create(RegisterView.class))));
    }

    @Subscribe
    public void onRegistered(RegisteredEvent event) {
        String message = "Registered! use your new account to login.";
        String caption = AppConfig.getDefault().getAppName();
        post(new ShowMessageBoxEvent(message, caption, MessageType.Info));
    }

    @Subscribe
    public void onClientShutdown(ClientShutdownEvent event) {
        runOnUiThread(() -> {
            cleanResources();
            if (event.getReason() != null) {
                String message = event.getReason().getMessage();
                String caption = AppConfig.getDefault().getAppName();
                post(new ShowMessageBoxEvent(message, caption, MessageType.Error));
            }
        }, false);
    }

    @Subscribe
    public void onAppShutdown(AppShutdownEvent event) {
        cleanResources();
        getPlatform().exit();
    }

    private void cleanResources() {
        StreamUtils.tryCloseStream(client);
        chattingManager.stop();
    }
}
