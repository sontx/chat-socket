package com.blogspot.sontx.chatsocket.client;

public class AppClientImpl implements AppClient {
    @Override
    public void start() throws Exception {
        Application.setSystemLookAndFeel();
        Application.run();
    }
}
