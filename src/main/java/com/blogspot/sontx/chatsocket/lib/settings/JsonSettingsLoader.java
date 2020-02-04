package com.blogspot.sontx.chatsocket.lib.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
public final class JsonSettingsLoader implements SettingsLoader {
    private final ConcurrentHashMap<Class<?>, Object> settings = new ConcurrentHashMap<>();
    private SettingsFile settingsFile;
    private boolean loadedFromFile = false;
    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSettingsLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(Class<T> clazz) {
        if (!loadedFromFile) {
            try {
                loadFromFile();
                loadedFromFile = true;
            } catch (IOException e) {
                log.error("Error while loading settings file", e);
            }
        }


        if (settings.containsKey(clazz)) {
            return (T) settings.get(clazz);
        }

        try {
            T found = cacheType(clazz);
            if (found != null)
                return found;
        } catch (JsonProcessingException e) {
            log.error("Error while parsing settings file", e);
        }

        try {
            T newObj = (T) clazz.getConstructors()[0].newInstance();
            settings.put(clazz, newObj);
            return newObj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error while construct new settings instance", e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T cacheType(Class<T> clazz) throws JsonProcessingException {
        if (settingsFile != null) {
            String key = getKey(clazz);
            Optional<Element> found = Arrays
                    .stream(settingsFile.settings)
                    .filter(element -> element.key.equals(key))
                    .findFirst();
            if (found.isPresent()) {
                Object obj = found.get().data;
                T value;
                if (obj instanceof LinkedHashMap) {
                    value = objectMapper.convertValue(obj, clazz);
                } else {
                    value = (T) obj;
                }
                settings.put(clazz, value);
                return value;
            }
        }
        return null;
    }

    private String getKey(Class<?> clazz) {
        Setting settingAnnotation = clazz.getAnnotation(Setting.class);
        return settingAnnotation != null
                ? settingAnnotation.key()
                : CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazz.getSimpleName());
    }

    private void loadFromFile() throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            settingsFile = objectMapper.readValue(file, SettingsFile.class);
        }
    }

    @Override
    public synchronized void save() {
        if (StringUtils.isEmpty(fileName))
            return;

        SettingsFile newSettingsFile = collectSettings();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    log.error("Can not delete settings file.");
                    return;
                }
            }

            objectMapper
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValue(file, newSettingsFile);

        } catch (IOException e) {
            log.error("Error while saving settings to file", e);
        }
    }

    private SettingsFile collectSettings() {
        SettingsFile newSettingsFile = new SettingsFile();
        newSettingsFile.modified = new Date();
        List<Element> elements = settingsFile != null
                ? new ArrayList<>(Arrays.asList(settingsFile.settings))
                : new ArrayList<>();
        for (Class<?> item : settings.keySet()) {
            updateOrAdd(elements, item, settings.get(item));
        }
        newSettingsFile.settings = new Element[elements.size()];
        newSettingsFile.settings = elements.toArray(newSettingsFile.settings);
        return newSettingsFile;
    }

    private void updateOrAdd(List<Element> elements, Class<?> item, Object value) {
        String key = getKey(item);
        for (Element element : elements) {
            if (element.key.equals(key)) {
                element.data = value;
                return;
            }
        }

        Element newElement = new Element();
        newElement.key = key;
        newElement.data = value;
        elements.add(newElement);
    }

    @Data
    private static class SettingsFile {
        private Date modified;
        private Element[] settings;
    }

    @Data
    private static class Element {
        private String key;
        private Object data;
    }
}
