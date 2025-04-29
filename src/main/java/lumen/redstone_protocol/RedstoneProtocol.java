package lumen.redstone_protocol;

import lumen.RPItemsGroup;
import lumen.redstone_protocol.block.RPBlocks;
import lumen.redstone_protocol.block_entity.RPBlockEntities;
import lumen.redstone_protocol.effect.RPEffects;
import lumen.redstone_protocol.entities.RPEntities;
import lumen.redstone_protocol.item.RPItems;
import lumen.redstone_protocol.network.RPNetwork;
import lumen.redstone_protocol.screen_handler.RPScreenHandler;
import lumen.redstone_protocol.sounds.RPSoundEvents;
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

        RPNetwork.registerNetwork();

        RPItemsGroup.registerItemsGroup();

        RPItems.registerItems();
        RPBlocks.initialize();

        RPEntities.registerEntities();
        RPBlockEntities.registerBlockEntities();
        RPEffects.registerEffects();
        RPSoundEvents.registerSounds();

        RPScreenHandler.registerScreenHandler();

        ServerTickEvents.END_SERVER_TICK.register(server -> ElevatorCooldownHandler.tickCooldowns());
    }
}