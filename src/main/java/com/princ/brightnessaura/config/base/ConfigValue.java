package com.princ.brightnessaura.config.base;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigValue<T> {
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public ConfigValue(Supplier<T> getter, Consumer<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public T get() {
        return getter.get();
    }

    public void set(T value) {
        setter.accept(value);
    }
}
