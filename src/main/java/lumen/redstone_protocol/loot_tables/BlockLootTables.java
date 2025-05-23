package lumen.redstone_protocol.loot_tables;

import lumen.redstone_protocol.block.RPBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockLootTables extends FabricBlockLootTableProvider {
    public BlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(RPBlocks.LOW_FRICTION_GLASS);
        addDrop(RPBlocks.IDEAL_ORBIT);

        addDrop(RPBlocks.PRIMARY_SPEED_MULTIPLIER);
        addDrop(RPBlocks.PRIMARY_VERTICAL_BOOSTER);
        addDrop(RPBlocks.PRIMARY_HORIZONTAL_BOOSTER);
        addDrop(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER);
        addDrop(RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER);
        addDrop(RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER);
        addDrop(RPBlocks.ADVANCED_SPEED_MULTIPLIER);
        addDrop(RPBlocks.ADVANCED_VERTICAL_BOOSTER);
        addDrop(RPBlocks.ADVANCED_HORIZONTAL_BOOSTER);
        addDrop(RPBlocks.CONTROLLABLE_HORIZONTAL_BOOST_BLOCK);
        addDrop(RPBlocks.CONTROLLABLE_VERTICAL_BOOST_BLOCK);
        addDrop(RPBlocks.UNLIMITED_BOOSTER);

        addDrop(RPBlocks.ITEM_PACKER);
        addDrop(RPBlocks.ITEM_UNPACKER);
        addDrop(RPBlocks.ITEM_CALIBRATOR);
        addDrop(RPBlocks.ITEM_STEERING_GEAR);
        addDrop(RPBlocks.ITEM_RESTARTING);
        addDrop(RPBlocks.JUMP_ENHANCER);

        addDrop(RPBlocks.PLAYER_TRANSPARENT_BLOCK);
        addDrop(RPBlocks.MOB_TRANSPARENT_BLOCK);
        addDrop(RPBlocks.ITEM_TRANSPARENT_BLOCK);
        addDrop(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK);
        addDrop(RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK);

        addDrop(RPBlocks.CONTROLLABLE_TRANSPARENT_STONE);
        addDrop(RPBlocks.TRACTOR);
        addDrop(RPBlocks.LASER_GENERATOR);
        addDrop(RPBlocks.ACTIVE_DEFENSE);
        addDrop(RPBlocks.RESTRAINING_FORCE_BLOCK);
        addDrop(RPBlocks.BIOLOGICAL_FIELD);
        addDrop(RPBlocks.ITEM_COLLECTOR_BLOCK);
        addDrop(RPBlocks.EXPLOSION_ABSORBER_BLOCK);
        addDrop(RPBlocks.ELEVATOR_BLOCK);
    }
}
