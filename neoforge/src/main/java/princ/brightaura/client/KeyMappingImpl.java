package princ.brightaura.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;

import static princ.brightaura.client.Constants.*;

@EventBusSubscriber(modid = NAMESPACE, value = Dist.CLIENT)
public class KeyMappingImpl {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(withDefaultNamespace("general"));
    public static final Lazy<KeyMapping> key = Lazy.of(() -> new KeyMapping(withKeyMappingPrefix("key"), InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY));

    @SubscribeEvent
    public static void registerAll(RegisterKeyMappingsEvent event) {
        event.register(key.get());
    }

    public static void listenClicks(ClientTickEvent.Post event) {
        if (key.get().consumeClick()) {
            adjustGamma(BRIGHTNESS_STATE, 15.0, 1.0, config.transition, config.transitionTime);
        }
        handleGammaTransition(BRIGHTNESS_STATE, config.transition);
    }
}
