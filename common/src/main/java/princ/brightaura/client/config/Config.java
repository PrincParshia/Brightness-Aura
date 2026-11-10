package princ.brightaura.client.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static princ.brightaura.client.Constants.LOG;

public class Config {
    private Path configPath;

    public boolean transition = true;
    public int transitionDuration = 30;

    public void init(Path configDir) {
        this.configPath = configDir.resolve("brightness_aura.toml");
        load();
    }

    public void load() {
        if (!Files.exists(this.configPath)) {
            save();
            return;
        }
        try {
            for (String line : Files.readAllLines(this.configPath)) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;
                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;
                switch (parts[0].trim()) {
                    case "transition" -> this.transition = Boolean.parseBoolean(parts[1].trim());
                    case "transitionDuration" -> this.transitionDuration = Integer.parseInt(parts[1].trim());
                }
            }
        } catch (IOException e) {
            LOG.error("Failed to load config: {}", e.getMessage());
        }
    }

    public void save() {
        try {
            Files.writeString(this.configPath, """
                    transition = %s
                    transitionDuration = %d
                    """.formatted(this.transition, this.transitionDuration)
            );
        } catch (IOException e) {
            LOG.error("Failed to save config: {}", e.getMessage());
        }
    }
}
