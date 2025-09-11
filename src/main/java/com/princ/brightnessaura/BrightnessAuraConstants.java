package com.princ.brightnessaura;

import com.princ.brightnessaura.config.BrightnessAuraConfig;
import com.princ.brightnessaura.config.base.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class BrightnessAuraConstants {
    public static final String NAMESPACE = "brightness-aura";
    public static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(NAMESPACE + ".toml");
    public static final BrightnessAuraConfig config = ConfigManager.load(BrightnessAuraConfig.class, configPath);
}
