package com.princ.brightnessaura.config.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigReader {

    public static Map<String, Map<String, String>> read(Path path) throws IOException {
        Map<String, Map<String, String>> sections = new HashMap<>();
        List<String> lines = Files.readAllLines(path);

        String currentSection = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                sections.putIfAbsent(currentSection, new HashMap<>());
            } else if (currentSection != null && line.contains("=")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();
                sections.get(currentSection).put(key, value);
            }
        }

        return sections;
    }
}
