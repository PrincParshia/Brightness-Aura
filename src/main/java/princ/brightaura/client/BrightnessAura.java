package princ.brightaura.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrightnessAura implements ClientModInitializer {
	public static final String NAMESPACE = "brightness-aura";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

	public static final KeyMapping.Category CATEGORY;
	public static final KeyMapping KEY;

	@Override
	public void onInitializeClient() {
		this.registerKeyMappings();
	}

	void registerKeyMappings() {
		KeyMappingHelper.registerKeyMapping(KEY);
		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			OptionInstance<Double> gamma = minecraft.options.gamma();

			if (KEY.consumeClick()) {
				if (gamma.get() > 1.0) {
					gamma.set(1.0);
				} else gamma.set(15.0);
			}
		});
	}

	public static Identifier withDefaultNamespace(final String string) {
		return Identifier.fromNamespaceAndPath(NAMESPACE, string);
	}

	public static String withKeyMappingPrefix(String string) {
		return "key." + NAMESPACE + "." + string;
	}

	static {
		CATEGORY = new KeyMapping.Category(withDefaultNamespace("default"));
		KEY = new KeyMapping(withKeyMappingPrefix("toggle"), InputConstants.Type.KEYSYM, InputConstants.KEY_B, CATEGORY);
	}
}