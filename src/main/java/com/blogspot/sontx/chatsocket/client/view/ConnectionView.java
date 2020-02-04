package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface ConnectionView extends BaseView {
    void setConnectButtonClickListener(Runnable listener);

    String getServerIp();

    String getServerPort();

    void setServerIp(String serverIp);

    void setServerPort(int serverPort);
}
