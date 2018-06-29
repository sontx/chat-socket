package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.event.ConnectToServerEvent;
import com.blogspot.sontx.chatsocket.client.event.ConnectedToServerEvent;
import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ConnectionPresenter extends AbstractService implements Presenter {
    private final ConnectionView connectionView;

    public ConnectionPresenter(ConnectionView connectionView) {
        this.connectionView = connectionView;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        connectionView.setConnectButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid address.", "Connection", MessageType.Error);
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
        post(new ConnectToServerEvent(serverIp, Integer.parseInt(serverPortAsString)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectedToServer(ConnectedToServerEvent event) {
        stop();
        connectionView.closeWindow();
    }

    public void show() {
        start();
        connectionView.setTitle(String.format("Welcome to %s", AppConfig.getDefault().getAppName()));
        connectionView.showWindow();
    }
}
