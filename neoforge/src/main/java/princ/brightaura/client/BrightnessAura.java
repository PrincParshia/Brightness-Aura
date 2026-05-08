package princ.brightaura.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

import static princ.brightaura.client.Constants.config;

@Mod(value = Constants.NAMESPACE, dist = Dist.CLIENT)
public class BrightnessAura {

    public BrightnessAura(IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener(BrightnessAura::onClientTick);
        config.init(FMLPaths.CONFIGDIR.get());
        NeoForge.EVENT_BUS.addListener(BrightnessAura::onRegisterCommands);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        KeyMappingImpl.listenClicks(event);
    }

    public static void onRegisterCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("brightaura")
                        .then(Commands.literal("transition")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            config.transition = BoolArgumentType.getBool(context, "value");
                                            config.save();
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("transition_time")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            config.transitionTime = IntegerArgumentType.getInteger(context, "value");
                                            config.save();
                                            return 1;
                                        })
                                )
                        )
        );
    }
}