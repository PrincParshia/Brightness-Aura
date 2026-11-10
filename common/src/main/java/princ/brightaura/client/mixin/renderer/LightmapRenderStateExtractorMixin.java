package princ.brightaura.client.mixin.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static princ.brightaura.client.Constants.*;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {

    @ModifyExpressionValue(
            method = "extract",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/LightmapRenderStateExtractor;needsUpdate:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    boolean modifyNeedsUpdate(boolean original) {
        if (brightnessState().execTransition) {
            return true;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "extract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;",
                    ordinal = 1
            )
    )
    Object modifyGamma(Object original, LightmapRenderState renderState, float partialTicks) {
        DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
        return calcGamma(brightnessState(), config.transition, deltaTracker.getGameTimeDeltaTicks());
    }
}
