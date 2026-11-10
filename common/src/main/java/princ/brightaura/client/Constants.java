package princ.brightaura.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import princ.brightaura.client.config.Config;
import princ.brightaura.client.state.BrightnessState;

public class Constants {
    public static final String NAMESPACE = "brightness_aura";
    public static final String NAME = "Brightness Aura";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);
    public static final Config config = new Config();
    private static final BrightnessState BRIGHTNESS_STATE = new BrightnessState();

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

    public static BrightnessState brightnessState() {
        return BRIGHTNESS_STATE;
    }

    public static void toggle(BrightnessState state, double max, double min, boolean transition, int transitionDuration) {
        if (transition) {
            if (!state.execTransition) {
                state.current = gamma().get();
                state.start = state.current;
                state.end = !gamma().get().equals(max) ? max : min;
                state.progress = 0.0;
                state.transitionDuration = transitionDuration;
                state.execTransition = true;
            } else {
                state.start = state.current;
                state.end = state.end != max ? max : min;
                double progress = (state.current - min) / (max - min);
                state.transitionDuration = transitionDuration * (state.end == max ? 1.0 - progress : progress);
                state.progress = 0.0;
            }
        } else {
            double endVal = !gamma().get().equals(max) ? max : min;
            gamma().set(endVal);
        }
    }

    public static double calcGamma(BrightnessState state, boolean transition, float deltaTicks) {
        if (transition && state.execTransition) {
            state.progress += deltaTicks / state.transitionDuration;
            if (state.progress >= 1.0) {
                state.execTransition = false;
                state.current = state.end;
                gamma().set(state.end);
            } else {
                return state.current = Mth.lerp(state.progress, state.start, state.end);
            }
        }
        return gamma().get();
    }
}