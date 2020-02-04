package com.blogspot.sontx.chatsocket.lib;

import com.blogspot.sontx.chatsocket.lib.platform.Platform;

public interface Presenter {
    void show();

    void setPlatform(Platform platform);
}
