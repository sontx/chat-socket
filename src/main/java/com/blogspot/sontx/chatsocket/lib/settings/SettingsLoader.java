package com.blogspot.sontx.chatsocket.lib.settings;

public interface SettingsLoader {
    <T> T get(Class<T> settingType);

    void save();
}
