package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.ConnectToServerEvent;
import com.blogspot.sontx.chatsocket.client.event.ConnectedToServerEvent;
import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ConnectionPresenter {
    private final ConnectionView connectionView;

    public ConnectionPresenter(ConnectionView connectionView) {
        this.connectionView = connectionView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        connectionView.setConnectButtonClickListener(() -> {
            if (!verifyInputs())
                connectionView.showMessageBox("Invalid address.");
            else
                connect();
        });
    }

    private boolean verifyInputs() {
        String serverIp = connectionView.getServerIp();
        String serverPortAsString = connectionView.getServerPort();
        return !StringUtils.isEmpty(serverIp) && NumberUtils.isDigits(serverPortAsString);
    }

    private void connect() {
        String serverIp = connectionView.getServerIp();
        String serverPortAsString = connectionView.getServerPort();
        EventBus.getDefault().post(new ConnectToServerEvent(serverIp, Integer.parseInt(serverPortAsString)));
    }

    @Subscribe
    public void onConnectedToServer(ConnectedToServerEvent event) {
        EventBus.getDefault().unregister(this);
        connectionView.closeWindow();
    }

    public void show() {
        EventBus.getDefault().register(this);
        connectionView.setTitle(String.format("Welcome to %s", AppConfig.getDefault().getAppName()));
        connectionView.showWindow();
    }
}
