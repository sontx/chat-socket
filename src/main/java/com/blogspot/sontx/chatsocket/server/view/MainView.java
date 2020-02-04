package com.blogspot.sontx.chatsocket.server.view;

import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface MainView extends BaseView {
    String getIp();

    String getPort();

    void setIp(String ip);

    void setPort(int port);

    void setStartButtonText(String text);

    void setStartButtonListener(Runnable listener);
}
