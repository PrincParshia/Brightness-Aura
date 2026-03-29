package princ.brightaura.mixin.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import princ.brightaura.client.OptionInstanceImpl;

import static net.minecraft.client.Options.genericValueLabel;

@Mixin(Options.class)
public class OptionsMixin {
    @Shadow
    @Final
    @Mutable
    private OptionInstance<Double> gamma;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Options;gamma:Lnet/minecraft/client/OptionInstance;"
            )
    )
    void init(Options options, OptionInstance<Double> optionInstance) {
        this.gamma = new OptionInstance<>("options.gamma", OptionInstance.noTooltip(), (caption, value) -> {
            int progressValueToDisplay = (int)(value * (double)100.0F);
            if (progressValueToDisplay == 0) {
                return genericValueLabel(caption, Component.translatable("options.gamma.min"));
            } else if (progressValueToDisplay == 50) {
                return genericValueLabel(caption, Component.translatable("options.gamma.default"));
            } else {
                return progressValueToDisplay == 100 ? genericValueLabel(caption, Component.translatable("options.gamma.max")) : genericValueLabel(caption, progressValueToDisplay);
            }
        }, OptionInstanceImpl.UnitDouble.INSTANCE, (double) 0.5F, (value) -> {
        });
    }
}
