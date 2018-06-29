package com.blogspot.sontx.chatsocket.lib.service;

import com.blogspot.sontx.chatsocket.lib.platform.Platform;

public interface Service {
    int getId();

    void setPlatform(Platform platform);

    void start();

    void stop();
}
