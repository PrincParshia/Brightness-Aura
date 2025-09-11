package com.princ.brightnessaura.config.base;

import com.princ.brightnessaura.config.BrightnessAuraConfig;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.princ.brightnessaura.BrightnessAuraConstants.*;

public class ConfigManager {
    public static <T> T load(Class<T> configClass, Path configPath) {
        T configInstance = null;

        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();

            if (Files.notExists(configPath)) {
                writeDefault(configInstance, configPath);
            }

            Map<String, Map<String, String>> data = ConfigReader.read(configPath);

            for (Field sectionField : configClass.getDeclaredFields()) {
                sectionField.setAccessible(true);
                Object sectionInstance = sectionField.get(configInstance);
                String sectionName = sectionField.getName();

                if (data.containsKey(sectionName)) {
                    Map<String, String> sectionData = data.get(sectionName);

                    for (Field valueField : sectionField.getType().getDeclaredFields()) {
                        valueField.setAccessible(true);
                        if (sectionData.containsKey(valueField.getName())) {
                            Object value = castValue(sectionData.get(valueField.getName()), valueField.getType());
                            valueField.set(sectionInstance, value);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return configInstance;
    }

    public static void save(BrightnessAuraConfig config) {
        try {
            Map<String, Map<String, Object>> sections = new LinkedHashMap<>();

            for (Field sectionField : BrightnessAuraConfig.class.getDeclaredFields()) {
                sectionField.setAccessible(true);
                Object sectionInstance = sectionField.get(config);

                Map<String, Object> values = new LinkedHashMap<>();
                for (Field valueField : sectionField.getType().getDeclaredFields()) {
                    valueField.setAccessible(true);
                    Object val = valueField.get(sectionInstance);
                    values.put(valueField.getName(), val);
                }

                sections.put(sectionField.getName(), values);
            }

            writeToFile(sections);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(Map<String, Map<String, Object>> sections) throws IOException {
        List<String> lines = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> sectionEntry : sections.entrySet()) {
            lines.add("[" + sectionEntry.getKey() + "]");
            for (Map.Entry<String, Object> valueEntry : sectionEntry.getValue().entrySet()) {
                lines.add(valueEntry.getKey() + " = " + valueEntry.getValue());
            }
            lines.add("");
        }

        Files.write(configPath, lines);
    }

    private static Object castValue(String raw, Class<?> type) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(raw);
        if (type == float.class || type == Float.class) return Float.parseFloat(raw);
        if (type == double.class || type == Double.class) return Double.parseDouble(raw);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(raw);
        return raw;
    }

    private static void writeDefault(Object configInstance, Path configPath) throws IOException, IllegalAccessException {
        var lines = new java.util.ArrayList<String>();

        for (Field sectionField : configInstance.getClass().getDeclaredFields()) {
            sectionField.setAccessible(true);
            Object sectionInstance = sectionField.get(configInstance);

            lines.add("[" + sectionField.getName() + "]");
            for (Field valueField : sectionField.getType().getDeclaredFields()) {
                valueField.setAccessible(true);
                lines.add(valueField.getName() + " = " + valueField.get(sectionInstance));
            }
            lines.add("");
        }

        Files.createDirectories(configPath.getParent());
        Files.write(configPath, lines);
    }
}
