package com.blogspot.sontx.chatsocket.lib.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public final class Security {
    private static final Pattern userNamePattern = Pattern.compile("^[a-z0-9_-]{3,15}$");
    private static final Pattern passwordPattern = Pattern.compile("^.{6,40}$");

    private Security() {
    }

    public static boolean checkValidUsername(String username) {
        return username != null && userNamePattern.matcher(username).matches();
    }

    public static boolean checkValidPassword(String password) {
        return password != null && passwordPattern.matcher(password).matches();
    }

    public static boolean checkValidDisplayName(String display) {
        return display != null && display.trim().length() >= 3;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    private static String convertToSHA1(byte[] bytes) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            return bytesToHexString(md.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPasswordHash(String password) {
        if (password == null)
            return null;
        return convertToSHA1(password.getBytes(Charset.forName("UTF-8")));
    }
}
