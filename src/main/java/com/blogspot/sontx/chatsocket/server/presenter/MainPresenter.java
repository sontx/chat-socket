package com.blogspot.sontx.chatsocket.server.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.server.ServerSettings;
import com.blogspot.sontx.chatsocket.server.event.*;
import com.blogspot.sontx.chatsocket.server.view.LogView;
import com.blogspot.sontx.chatsocket.server.view.MainView;
import com.google.common.eventbus.Subscribe;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.LogManager;

@Log4j
public class MainPresenter extends AbstractPresenter<MainView> {
    private volatile boolean serverIsRunning;

    public MainPresenter(MainView mainView, LogView logView) {
        super(mainView);
        LogManager.getRootLogger().addAppender(new Log4jUIAppender(logView));
    }

    @Override
    protected  void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setStartButtonListener(() -> {
            if (serverIsRunning)
                stopServer();
            else
                startServer();
        });
    }

    @Override
    protected void onUserClosesView() {
        stop();
        post(new ShutdownServerEvent(ShutdownServerEvent.ALL));
        super.onUserClosesView();
    }

    private void stopServer() {
        post(new StopServerEvent());
    }

    private void startServer() {
        if (verifyInputs()) {
            String ip = view.getIp();
            int port = Integer.parseInt(view.getPort());

            post(new StartServerEvent(ip, port));

            ServerSettings settings = getSetting(ServerSettings.class);
            settings.setIp(ip);
            settings.setPort(port);
        }
    }

    private boolean verifyInputs() {
        String listenOnIp = view.getIp();
        String portAsString = view.getPort();
        if (!NumberUtils.isDigits(portAsString)) {
            postMessageBox("Port must be a number", MessageType.Error);
            return false;
        } else if (StringUtils.isEmpty(listenOnIp)) {
            postMessageBox("Ip must be not empty", MessageType.Error);
            return false;
        }
        return true;
    }

    public void show() {
        start();
        ServerSettings settings = getSetting(ServerSettings.class);
        view.setIp(settings.getIp());
        if (settings.getPort() > 0)
            view.setPort(settings.getPort());
        view.setMainWindow();
        view.setTitle(String.format("%s %s", AppConfig.getDefault().getAppName(), AppConfig.getDefault().getAppVersion()));
        view.showWindow();
    }

    @Subscribe
    public void onServerStatusChanged(ServerStatusChangedEvent event) {
        runOnUiThread(() -> {
            serverIsRunning = event.isRunning();
            view.setStartButtonText(serverIsRunning ? "Stop" : "Start");
        });
    }
}
