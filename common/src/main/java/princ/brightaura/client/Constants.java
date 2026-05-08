package princ.brightaura.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import princ.brightaura.client.config.Config;
import princ.brightaura.client.state.BrightnessState;

public class Constants {
	public static final String NAMESPACE = "brightness_aura";
	public static final String NAME = "Brightness Aura";
	public static final Logger LOG = LoggerFactory.getLogger(NAME);
	public static final Config config = new Config();
	public static final BrightnessState BRIGHTNESS_STATE = new BrightnessState();

	public static Identifier withDefaultNamespace(final String string) {
		return Identifier.fromNamespaceAndPath(NAMESPACE, string);
	}

	public static String withKeyMappingPrefix(final String string) {
		return "key." + NAMESPACE + "." + string;
	}

	public static Minecraft minecraft() {
		return Minecraft.getInstance();
	}

	public static OptionInstance<Double> gamma() {
		return minecraft().options.gamma();
	}

	public static void adjustGamma(BrightnessState state, double max, double min, boolean transition, int transitionTime) {
		if (transition) {
			if (!state.execTransition) {
				state.targetVal = computeTargetVal(gamma().get(), max, min);
				state.transitionTime = transitionTime;
				state.elapsedTransitionTime = 0;
				state.execTransition = true;
			} else {
				state.targetVal = computeTargetVal(state.targetVal, max, min);
				state.transitionTime = state.elapsedTransitionTime;
				state.elapsedTransitionTime = 0;
			}
		} else {
			double targetVal = computeTargetVal(gamma().get(), max, min);
			gamma().set(targetVal);
		}
	}

	public static void handleGammaTransition(BrightnessState state, boolean transition) {
		if (transition && state.execTransition) {
			if (state.transitionTime <= 0) {
				state.execTransition = false;
			} else {
				gamma().set(lerp(gamma().get(), state.targetVal, state.transitionTime));
				state.transitionTime--;
				state.elapsedTransitionTime++;
			}
		}
	}

	static double lerp(double current, double val, int transitionTime) {
		return current + ((val - current) / transitionTime);
	}

	static double computeTargetVal(double current, double val, double prev) {
		return current != val ? val : prev;
	}
}