package lumen.redstone_protocol.item;

import lumen.redstone_protocol.RedstoneProtocol;
import lumen.redstone_protocol.block.RPBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static lumen.redstone_protocol.RedstoneProtocol.CUSTOM_ITEM_GROUP_KEY;

public class RPItems {
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register((itemGroup) -> {
            itemGroup.add(RPBlocks.LOW_FRICTION_GLASS.asItem());
            itemGroup.add(RPBlocks.IDEAL_ORBIT.asItem());

            itemGroup.add(RPBlocks.PLAYER_TRANSPARENT_BLOCK.asItem());
            itemGroup.add(RPBlocks.MOB_TRANSPARENT_BLOCK.asItem());
            itemGroup.add(RPBlocks.ITEM_TRANSPARENT_BLOCK.asItem());
            itemGroup.add(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK.asItem());
            itemGroup.add(RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK.asItem());

            itemGroup.add(RPBlocks.PRIMARY_SPEED_MULTIPLIER.asItem());
            itemGroup.add(RPBlocks.PRIMARY_HORIZONTAL_BOOSTER.asItem());
            itemGroup.add(RPBlocks.PRIMARY_VERTICAL_BOOSTER.asItem());

            itemGroup.add(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER.asItem());
            itemGroup.add(RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER.asItem());
            itemGroup.add(RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER.asItem());

            itemGroup.add(RPBlocks.ADVANCED_SPEED_MULTIPLIER.asItem());
            itemGroup.add(RPBlocks.ADVANCED_HORIZONTAL_BOOSTER.asItem());
            itemGroup.add(RPBlocks.ADVANCED_VERTICAL_BOOSTER.asItem());
            itemGroup.add(RPBlocks.UNLIMITED_BOOSTER.asItem());

            itemGroup.add(RPBlocks.CONTROLLABLE_HORIZONTAL_BOOST_BLOCK.asItem());
            itemGroup.add(RPBlocks.CONTROLLABLE_VERTICAL_BOOST_BLOCK.asItem());
            itemGroup.add(RPBlocks.CONTROLLABLE_TRANSPARENT_STONE.asItem());

            itemGroup.add(RPBlocks.ITEM_PACKER.asItem());
            itemGroup.add(RPBlocks.ITEM_UNPACKER.asItem());
            itemGroup.add(RPBlocks.ITEM_CALIBRATOR.asItem());
            itemGroup.add(RPBlocks.ITEM_STEERING_GEAR.asItem());
            itemGroup.add(RPBlocks.JUMP_ENHANCER.asItem());

            itemGroup.add(RPBlocks.TRACTOR.asItem());
            itemGroup.add(RPBlocks.LASER_GENERATOR.asItem());
        });
    }

    public static net.minecraft.item.Item register(String path, Item item) {
        Identifier itemID = Identifier.of(RedstoneProtocol.MOD_ID, path);
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
