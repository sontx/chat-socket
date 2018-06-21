package com.blogspot.sontx.chatsocket.server.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Log4j
public class MainPresenter {
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
            EventBus.getDefault().unregister(this);
            EventBus.getDefault().post(new ShutdownServerEvent(ShutdownServerEvent.ALL));
        });
    }

    private void stopServer() {
        EventBus.getDefault().post(new StopServerEvent());
    }

    private void startServer() {
        if (verifyInputs()) {
            EventBus.getDefault().post(new StartServerEvent(mainView.getIp(), Integer.parseInt(mainView.getPort())));
        }
    }

    private boolean verifyInputs() {
        String listenOnIp = mainView.getIp();
        String portAsString = mainView.getPort();
        if (!NumberUtils.isDigits(portAsString)) {
            mainView.showMessageBox("Port must be a number");
            return false;
        } else if (StringUtils.isEmpty(listenOnIp)) {
            mainView.showMessageBox("Ip must be not empty");
            return false;
        }
        return true;
    }

    public void show() {
        EventBus.getDefault().register(this);
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
