package lumen.redstone_protocol;

import lumen.redstone_protocol.block.RPBlocks;
import lumen.redstone_protocol.block_entity.RPBlockEntities;
import lumen.redstone_protocol.screen_handler.RPScreenHandler;
import lumen.redstone_protocol.util.ElevatorCooldownHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstoneProtocol implements ModInitializer {
    public static final String MOD_ID = "redstone-protocol";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Redstone Protocol");

        RPItemsGroup.registerItemsGroup();

        RPBlocks.initialize();

        RPBlockEntities.registerBlockEntities();
        RPScreenHandler.registerScreenHandler();

        ServerTickEvents.END_SERVER_TICK.register(server -> ElevatorCooldownHandler.tickCooldowns());
    }
}