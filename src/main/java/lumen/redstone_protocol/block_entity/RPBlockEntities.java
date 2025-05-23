package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.RedstoneProtocol;
import lumen.redstone_protocol.block.RPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RPBlockEntities {
    public static final BlockEntityType<LaserGenEntity> LASER_GEN_ENTITY = register(
            "laser_generator", LaserGenEntity::new, RPBlocks.LASER_GENERATOR);

    public static final BlockEntityType<TractorBlockEntity> TRACTOR_ENTITY = register(
            "tractor", TractorBlockEntity::new, RPBlocks.TRACTOR);

    public static final BlockEntityType<ActiveDefenseBlockEntity> ACTIVE_DEFENSE_BLOCK_ENTITY = register(
            "active_defense", ActiveDefenseBlockEntity::new, RPBlocks.ACTIVE_DEFENSE
    );

    public static final BlockEntityType<RestrainingForceBlockEntity> RESTRAINING_FORCE_BLOCK_ENTITY_BLOCK_ENTITY = register(
            "restraining_force_field", RestrainingForceBlockEntity::new, RPBlocks.RESTRAINING_FORCE_BLOCK
    );

    public static final BlockEntityType<ItemCollectorBlockEntity> ITEM_COLLECTOR_BLOCK_ENTITY_BLOCK_ENTITY = register(
            "item_collector_block", ItemCollectorBlockEntity::new, RPBlocks.ITEM_COLLECTOR_BLOCK
    );

    public static final BlockEntityType<ElevatorBlockEntity> ELEVATOR_BLOCK_ENTITY = register(
            "elevator_block", ElevatorBlockEntity::new, RPBlocks.ELEVATOR_BLOCK
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String id,
            BlockEntityType.BlockEntityFactory<? extends T> entityFactory,
            Block... blocks) {

        return Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(RedstoneProtocol.MOD_ID, id),
                BlockEntityType.Builder.<T>create(entityFactory, blocks).build()
        );
    }

    public static void registerBlockEntities() {
    }
}
