package princ.brightaura.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

import static princ.brightaura.client.Constants.*;

public class KeyMappingImpl {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(withDefaultNamespace("general"));
    public static final KeyMapping key = new KeyMapping(withKeyMappingPrefix("key"), InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);

    public static void registerAll() {
        KeyMappingHelper.registerKeyMapping(key);
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (key.consumeClick()) {
                adjustGamma(BRIGHTNESS_STATE, 15.0, 1.0, config.transition, config.transitionTime);
            }
            handleGammaTransition(BRIGHTNESS_STATE, config.transition);
        });
    }
}
