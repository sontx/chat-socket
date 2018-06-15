package com.blogspot.sontx.chatsocket;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j
public final class AppConfig {
    private static AppConfig defaultInstance;

    public synchronized static AppConfig getDefault() {
        if (defaultInstance == null) {
            try {
                try (InputStream inputStream = AppConfig.class.getResourceAsStream("/app.properties")) {
                    defaultInstance = new AppConfig(inputStream);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return defaultInstance;
    }

    private final Properties properties;

    public String getAppName() {
        return properties.getProperty("AppName");
    }

    private AppConfig(InputStream configInputStream) throws IOException {
        properties = new Properties();
        properties.load(configInputStream);
    }
}
