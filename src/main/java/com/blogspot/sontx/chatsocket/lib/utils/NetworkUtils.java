package com.blogspot.sontx.chatsocket.lib.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class NetworkUtils {

    public static List<String> getAllAddresses() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ignored) {
            return Collections.emptyList();
        }

        List<String> ips = new ArrayList<>();
        while (interfaces.hasMoreElements()) {
            NetworkInterface netInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address.getAddress().length == 4)
                    ips.add(address.getHostAddress());
            }
        }
        return ips;
    }

    private NetworkUtils() {

    }
}
