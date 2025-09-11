package com.princ.brightnessaura.config;

import com.princ.brightnessaura.config.base.ConfigValue;

public class BrightnessAuraConfig {
    public General general = new General();

    public static class General {
        private boolean enabled = true;
        private double lastvalue = 0.5;

        public ConfigValue<Double> lastValue() {
            return new ConfigValue<>(
                    () -> lastvalue,
                    v -> lastvalue = v
            );
        }

        public ConfigValue<Boolean> enabled() {
            return new ConfigValue<>(
                    () -> enabled,
                    v -> enabled = v
            );
        }
    }
}
