package com.blogspot.sontx.chatsocket.lib.view;

public interface BaseView {
    void showWindow();

    void closeWindow();

    void setTitle(String title);

    void showMessageBox(String message);

    void setOnClosingListener(Runnable listener);

    void setMainWindow();
}
