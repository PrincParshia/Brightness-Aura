package com.princ.brightnessaura.client;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance.SliderableValueSet;

import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;

@Environment(EnvType.CLIENT)
public class OptionInstanceImpl {
    @Environment(EnvType.CLIENT)
    public static enum UnitDouble implements SliderableValueSet<Double> {
        INSTANCE;

        public Optional<Double> validateValue(Double double_) {
            return double_ >= (double) 0.0F && double_ <= (double) 15.0F ? Optional.of(double_) : Optional.empty();
        }

        public double toSliderValue(Double double_) {
            return double_;
        }

        public Double fromSliderValue(double d) {
            return d;
        }

        public <R> SliderableValueSet<R> xmap(final DoubleFunction<? extends R> doubleFunction, final ToDoubleFunction<? super R> toDoubleFunction) {
            return new SliderableValueSet<R>() {
                public Optional<R> validateValue(R object) {
                    Objects.requireNonNull(doubleFunction);
                    return UnitDouble.this.validateValue(toDoubleFunction.applyAsDouble(object)).map(doubleFunction::apply);
                }

                public double toSliderValue(R object) {
                    return UnitDouble.this.toSliderValue(toDoubleFunction.applyAsDouble(object));
                }

                public R fromSliderValue(double d) {
                    return (R) doubleFunction.apply(UnitDouble.this.fromSliderValue(d));
                }

                public Codec<R> codec() {
                    Objects.requireNonNull(doubleFunction);
                    Objects.requireNonNull(toDoubleFunction);
                    return UnitDouble.this.codec().xmap(doubleFunction::apply, toDoubleFunction::applyAsDouble);
                }
            };
        }

        public Codec<Double> codec() {
            return Codec.withAlternative(Codec.doubleRange((double) 0.0F, (double) 15.0F), Codec.BOOL, (boolean_) -> boolean_ ? (double) 15.0F : (double) 0.0F);
        }
    }
}
