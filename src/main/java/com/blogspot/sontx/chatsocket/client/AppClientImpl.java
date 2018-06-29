package com.blogspot.sontx.chatsocket.client;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.*;
import com.blogspot.sontx.chatsocket.client.model.*;
import com.blogspot.sontx.chatsocket.client.model.handler.ResponseHandlerFactory;
import com.blogspot.sontx.chatsocket.client.presenter.*;
import com.blogspot.sontx.chatsocket.client.view.*;
import com.blogspot.sontx.chatsocket.lib.platform.Platform;
import com.blogspot.sontx.chatsocket.lib.service.event.ShowMessageBoxEvent;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.thread.JavaFxInvoker;
import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.blogspot.sontx.chatsocket.lib.view.WindowUtils;
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
    private ChattingManager chattingManager;
    private Platform platform;

    @Override
    public void start(Platform platform) {
        this.platform = platform;
        registerEventBus(platform);
        initializeChattingManager(platform);
        showUI();
    }

    private void registerEventBus(Platform platform) {
        platform.getEventBus().register(this);
    }

    private void initializeChattingManager(Platform platform) {
        chattingManager = new ChattingManagerImpl();
        ((ChattingManagerImpl) chattingManager).setPlatform(platform);
    }

    private void showUI() {
        WindowUtils.setSystemLookAndFeel();
        openConnectionWindow();
    }

    private void openConnectionWindow() {
        startPresenter(new ConnectionPresenter(create(ConnectionView.class)));
    }

    private void startPresenter(Presenter presenter) {
        presenter.setPlatform(platform);
        presenter.show();
    }

    private <T> T create(Class<T> viewType) {
        return new JavaFxInvoker.Helper<T>().invokeWithResult(() -> platform.getViewFactory().create(viewType));
    }

    @Subscribe
    public void onConnectToServer(ConnectToServerEvent event) {
        StreamUtils.tryCloseStream(client);
        chattingManager.start();
        client = new SocketClient(
                event.getServerIp(),
                event.getServerPort(),
                new ResponseHandlerFactory(chattingManager, platform));
        ((SocketClient) client).setPlatform(platform);
        client.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onConnectedToServer(ConnectedToServerEvent event) {
        openLoginWindow();
    }

    private void openLoginWindow() {
        startPresenter(new LoginPresenter(create(LoginView.class)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogged(LoggedEvent event) {
        userProfile.update(event.getLoggedAccount());
        openMainWindow();
    }

    private void openMainWindow() {
        FriendListPresenter friendListPresenter = new FriendListPresenter(create(FriendListView.class));
        friendListPresenter.setMyAccountInfo(userProfile);
        startPresenter(friendListPresenter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onOpenChat(OpenChatEvent event) {
        startPresenter(new ChatPresenter(create(ChatView.class), event.getChatWith()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onOpenMyProfile(OpenMyProfileEvent event) {
        startPresenter(new ProfilePresenter(create(ProfileView.class), userProfile));
    }

    @Subscribe
    public void onLoggin(LoginEvent event) {
        userProfile.setUsername(event.getUsername());
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onOpenRegister(OpenRegisterEvent event) {
        startPresenter(new RegisterPresenter(create(RegisterView.class)));
    }

    @Subscribe
    public void onRegistered(RegisteredEvent event) {
        String message = "Registered! use your new account to login.";
        String caption = AppConfig.getDefault().getAppName();
        platform.getEventBus().post(new ShowMessageBoxEvent(message, caption, MessageType.Info));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientShutdown(ClientShutdownEvent event) {
        cleanResources();
        if (event.getReason() != null) {
            String message = event.getReason().getMessage();
            String caption = AppConfig.getDefault().getAppName();
            platform.getEventBus().post(new ShowMessageBoxEvent(message, caption, MessageType.Error));
        }
    }

    @Subscribe
    public void onAppShutdown(AppShutdownEvent event) {
        cleanResources();
        platform.exit();
    }

    private void cleanResources() {
        StreamUtils.tryCloseStream(client);
        chattingManager.stop();
    }
}
