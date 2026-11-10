package princ.brightaura.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.loader.api.FabricLoader;
import princ.brightaura.client.config.Config;

import static princ.brightaura.client.Constants.config;

public class BrightnessAura implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMappingImpl.registerAll();
        config.init(FabricLoader.getInstance().getConfigDir());
        this.registerCommands(config);
    }

    public void registerCommands(Config config) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            dispatcher.register(
                    ClientCommands.literal("brightaura")
                            .then(ClientCommands.literal("transition")
                                    .then(ClientCommands.argument("value", BoolArgumentType.bool())
                                            .executes(context -> {
                                                config.transition = BoolArgumentType.getBool(context, "value");
                                                config.save();
                                                return 1;
                                            })
                                    )
                            )
                            .then(ClientCommands.literal("transition_duration")
                                    .then(ClientCommands.argument("ticks", IntegerArgumentType.integer(1))
                                            .executes(context -> {
                                                config.transitionDuration = IntegerArgumentType.getInteger(context, "ticks");
                                                config.save();
                                                return 1;
                                            })
                                    )
                            )
            );
        });
    }
}
