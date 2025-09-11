package com.princ.brightnessaura.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.princ.brightnessaura.config.base.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import static com.princ.brightnessaura.BrightnessAuraConstants.*;

public class BrightnessAura implements ClientModInitializer {
    public static final KeyMapping toggleKey = new KeyMapping("key." + NAMESPACE + ".gamma", InputConstants.Type.KEYSYM, InputConstants.KEY_B, "key.categories." + NAMESPACE);

	@Override
	public void onInitializeClient() {
        registerKeyMappings();
	}

    private static void registerKeyMappings() {
        KeyBindingHelper.registerKeyBinding(toggleKey);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (toggleKey.consumeClick()) {
                if (minecraft.options.gamma().get() > 1.0) {
                    minecraft.options.gamma().set(config.general.lastValue().get());
                    config.general.enabled().set(false);

                } else {
                    config.general.lastValue().set(minecraft.options.gamma().get());
                    minecraft.options.gamma().set(15.0);
                    config.general.enabled().set(true);
                }
                ConfigManager.save(config);
            }
        });
    }
}