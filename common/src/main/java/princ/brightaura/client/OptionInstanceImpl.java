package princ.brightaura.client;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;

import java.util.Optional;

public class OptionInstanceImpl {
    public enum UnitDouble implements OptionInstance.SliderableValueSet<Double> {
        INSTANCE;

        public Optional<Double> validateValue(Double value) {
            return value >= (double) 0.0F && value <= (double) 15.0F ? Optional.of(value) : Optional.empty();
        }

        public double toSliderValue(Double value) {
            return value;
        }

        public Double fromSliderValue(double slider) {
            return slider;
        }

        public Codec<Double> codec() {
            return Codec.withAlternative(Codec.doubleRange(0.0F, 15.0F), Codec.BOOL, (b) -> b ? (double) 15.0F : (double) 0.0F);
        }
    }
}
