package com.blogspot.sontx.chatsocket.server.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.server.event.AppShutdownEvent;
import com.blogspot.sontx.chatsocket.server.event.ServerStatusChangedEvent;
import com.blogspot.sontx.chatsocket.server.event.ShutdownServerEvent;
import com.blogspot.sontx.chatsocket.server.event.StartServerEvent;
import com.blogspot.sontx.chatsocket.server.event.StopServerEvent;
import com.blogspot.sontx.chatsocket.server.view.LogView;
import com.blogspot.sontx.chatsocket.server.view.MainView;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.LogManager;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Log4j
public class MainPresenter extends AbstractService {
    private final MainView mainView;
    private volatile boolean serverIsRunning;

    public MainPresenter(MainView mainView, LogView logView) {
        this.mainView = mainView;
        LogManager.getRootLogger().addAppender(new Log4jUIAppender(logView));
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        mainView.setStartButtonListener(() -> {
            if (serverIsRunning)
                stopServer();
            else
                startServer();
        });
        mainView.setOnClosingListener(() -> {
            stop();
            post(new ShutdownServerEvent(ShutdownServerEvent.ALL));
            post(new AppShutdownEvent());
        });
    }

    private void stopServer() {
        post(new StopServerEvent());
    }

    private void startServer() {
        if (verifyInputs()) {
            post(new StartServerEvent(mainView.getIp(), Integer.parseInt(mainView.getPort())));
        }
    }

    private boolean verifyInputs() {
        String listenOnIp = mainView.getIp();
        String portAsString = mainView.getPort();
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
        mainView.setMainWindow();
        mainView.setTitle(String.format("%s %s", AppConfig.getDefault().getAppName(), AppConfig.getDefault().getAppVersion()));
        mainView.showWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerStatusChanged(ServerStatusChangedEvent event) {
        serverIsRunning = event.isRunning();
        mainView.setStartButtonText(serverIsRunning ? "Stop" : "Start");
    }
}
