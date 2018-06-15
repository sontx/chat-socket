package com.blogspot.sontx.chatsocket.client.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectToServerEvent {
    private String serverIp;
    private int serverPort;
}
