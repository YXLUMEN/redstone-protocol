package lumen.redstone_protocol.screen_handler;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class RPScreenHandler {
    public static final ScreenHandlerType<ItemCollectorScreenHandler> ITEM_COLLECTOR_SCREEN_HANDLER_SCREEN_HANDLER_TYPE = Registry.register(
            Registries.SCREEN_HANDLER, Identifier.of(RedstoneProtocol.MOD_ID, "item_collector_block"),
            new ScreenHandlerType<>(ItemCollectorScreenHandler::new, FeatureSet.empty()));

    public static void registerScreenHandler() {
    }
}
