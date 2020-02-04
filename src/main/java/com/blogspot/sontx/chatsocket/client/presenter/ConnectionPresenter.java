package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.client.ClientSettings;
import com.blogspot.sontx.chatsocket.client.event.ConnectToServerEvent;
import com.blogspot.sontx.chatsocket.client.event.ConnectedToServerEvent;
import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ConnectionPresenter extends AbstractPresenter<ConnectionView> {
    public ConnectionPresenter(ConnectionView connectionView) {
        super(connectionView);
    }

    @Override
    protected void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setConnectButtonClickListener(() -> {
            if (!verifyInputs())
                postMessageBox("Invalid address.", "Connection", MessageType.Error);
            else
                connect();
        });
    }

    private boolean verifyInputs() {
        String serverIp = view.getServerIp();
        String serverPortAsString = view.getServerPort();
        return !StringUtils.isEmpty(serverIp) && NumberUtils.isDigits(serverPortAsString);
    }

    private void connect() {
        String serverIp = view.getServerIp();
        int serverPort = Integer.parseInt(view.getServerPort());

        post(new ConnectToServerEvent(serverIp, serverPort));

        ClientSettings settings = getSetting(ClientSettings.class);
        settings.setServerIp(serverIp);
        settings.setServerPort(serverPort);
    }

    @Subscribe
    public void onConnectedToServer(ConnectedToServerEvent event) {
        runOnUiThread(() -> {
            stop();
            view.closeWindow();
        });
    }

    public void show() {
        start();
        ClientSettings settings = getSetting(ClientSettings.class);
        view.setServerIp(settings.getServerIp());
        if (settings.getServerPort() > 0)
            view.setServerPort(settings.getServerPort());
        view.setTitle(String.format("Welcome to %s", AppConfig.getDefault().getAppName()));
        view.showWindow();
    }
}
