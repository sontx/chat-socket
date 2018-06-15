package com.blogspot.sontx.chatsocket.server;

import com.blogspot.sontx.chatsocket.lib.utils.StreamUtils;
import com.blogspot.sontx.chatsocket.lib.view.MessageBox;
import com.blogspot.sontx.chatsocket.lib.view.WindowUtils;
import com.blogspot.sontx.chatsocket.server.event.ServerStatusChangedEvent;
import com.blogspot.sontx.chatsocket.server.event.StartServerEvent;
import com.blogspot.sontx.chatsocket.server.event.StopServerEvent;
import com.blogspot.sontx.chatsocket.server.model.RequestRouter;
import com.blogspot.sontx.chatsocket.server.model.Server;
import com.blogspot.sontx.chatsocket.server.model.SocketServer;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManager;
import com.blogspot.sontx.chatsocket.server.model.account.AccountManagerImpl;
import com.blogspot.sontx.chatsocket.server.model.account.JsonAccountStorage;
import com.blogspot.sontx.chatsocket.server.model.handler.RequestHandlerFactory;
import com.blogspot.sontx.chatsocket.server.presenter.MainPresenter;
import com.blogspot.sontx.chatsocket.server.view.MainWindow;
import lombok.extern.log4j.Log4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Default implementation of {@link Server} that uses:
 * <pre>
 * Connection: tcp socket.
 * Store: json file.
 * Serialization: basic java {@link java.io.Serializable} feature.
 * Components communication: eventbus.
 * </pre>
 */
@Log4j
public class AppServerImpl implements AppServer {
    private AccountManager accountManager;
    private RequestRouter requestRouter;
    private Server server;

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        initializeComponents();
        showUI();
    }

    private void initializeComponents() {
        initializeAccountManager();
        initializeRequestRouter();
    }

    private void initializeAccountManager() {
        try {
            accountManager = new AccountManagerImpl(new JsonAccountStorage("user.json"));
        } catch (IOException e) {
            log.error("Error while initializing account manager", e);
            MessageBox.show(null, "Can not initialize account manager", MessageBox.MESSAGE_ERROR);
        }
    }

    private void initializeRequestRouter() {
        if (accountManager != null) {
            requestRouter = new RequestRouter(new RequestHandlerFactory(accountManager));
        }
    }

    private void showUI() {
        WindowUtils.setSystemLookAndFeel();
        MainPresenter presenter = new MainPresenter(new MainWindow());
        presenter.show();
    }

    @Subscribe
    public void onStartServer(StartServerEvent event) {
        StreamUtils.tryCloseStream(server);
        try {
            startServer(event.getIp(), event.getPort());
            EventBus.getDefault().post(new ServerStatusChangedEvent(true));
        } catch (IOException e) {
            log.error("Error while staring server", e);
        }
    }

    private void startServer(String listenOnIp, int port) throws IOException {
        server = new SocketServer(port, listenOnIp, 100);
        server.start();

        if (requestRouter != null) {
            requestRouter.start();
        }
    }

    @Subscribe
    public void onStopServer(StopServerEvent event) {
        StreamUtils.tryCloseStream(server);
        if (requestRouter != null) {
            requestRouter.stop();
        }

        EventBus.getDefault().post(new ServerStatusChangedEvent(false));
    }
}
