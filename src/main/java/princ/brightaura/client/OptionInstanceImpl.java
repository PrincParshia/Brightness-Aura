package princ.brightaura.client;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public final class OptionInstanceImpl {
    @Environment(EnvType.CLIENT)
    public static enum UnitDouble implements OptionInstance.SliderableValueSet<Double> {
        INSTANCE;

        public Optional<Double> validateValue(final Double value) {
            return value >= (double) 0.0F && value <= (double) 15.0F ? Optional.of(value) : Optional.empty();
        }

        public double toSliderValue(final Double value) {
            return value;
        }

        public Double fromSliderValue(final double slider) {
            return slider;
        }

        public Codec<Double> codec() {
            return Codec.withAlternative(Codec.doubleRange(0.0F, 15.0F), Codec.BOOL, (b) -> b ? (double) 15.0F : (double) 0.0F);
        }
    }
}
