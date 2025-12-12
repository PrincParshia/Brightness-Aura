package princ.brightnessaura.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class BrightnessAura implements ClientModInitializer {
	public static final String NAMESPACE = "brightness-aura";
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(NAMESPACE, "misc"));
    public static final KeyMapping key = new KeyMapping("key." + NAMESPACE + ".gamma", InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);

	@Override
	public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(key);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (key.consumeClick()) {
                if (minecraft.options.gamma().get() > 1.0) {
                    minecraft.options.gamma().set(1.0);
                } else {
                    minecraft.options.gamma().set(15.0);
                }
            }
        });
	}
}