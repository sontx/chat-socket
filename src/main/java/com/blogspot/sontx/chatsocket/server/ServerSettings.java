package com.blogspot.sontx.chatsocket.server;

import com.blogspot.sontx.chatsocket.lib.settings.Setting;
import lombok.Data;

@Data
@Setting(key = "server")
public class ServerSettings {
    private String ip;
    private int port;
}
