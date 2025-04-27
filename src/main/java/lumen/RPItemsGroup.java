package lumen;

import lumen.redstone_protocol.RedstoneProtocol;
import lumen.redstone_protocol.block.RPBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static lumen.redstone_protocol.item.RPItems.FLASH_GRENADE;
import static lumen.redstone_protocol.item.RPItems.SMOKE_GRENADE;

public class RPItemsGroup {
    public static final ItemGroup REDSTONE_PROTOCOL_ITEM_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(RedstoneProtocol.MOD_ID, "redstone_protocol"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(RPBlocks.LOW_FRICTION_GLASS))
                    .displayName(Text.translatable("itemGroup.redstone-protocol"))
                    .entries((displayContext, entries) -> {
                        entries.add(RPBlocks.LOW_FRICTION_GLASS);
                        entries.add(RPBlocks.IDEAL_ORBIT);
                        entries.add(RPBlocks.PLAYER_TRANSPARENT_BLOCK);
                        entries.add(RPBlocks.MOB_TRANSPARENT_BLOCK);
                        entries.add(RPBlocks.ITEM_TRANSPARENT_BLOCK);
                        entries.add(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK);
                        entries.add(RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK);
                        entries.add(RPBlocks.PRIMARY_SPEED_MULTIPLIER);
                        entries.add(RPBlocks.PRIMARY_HORIZONTAL_BOOSTER);
                        entries.add(RPBlocks.PRIMARY_VERTICAL_BOOSTER);
                        entries.add(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER);
                        entries.add(RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER);
                        entries.add(RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER);
                        entries.add(RPBlocks.ADVANCED_SPEED_MULTIPLIER);
                        entries.add(RPBlocks.ADVANCED_HORIZONTAL_BOOSTER);
                        entries.add(RPBlocks.ADVANCED_VERTICAL_BOOSTER);
                        entries.add(RPBlocks.UNLIMITED_BOOSTER);
                        entries.add(RPBlocks.CONTROLLABLE_HORIZONTAL_BOOST_BLOCK);
                        entries.add(RPBlocks.CONTROLLABLE_VERTICAL_BOOST_BLOCK);
                        entries.add(RPBlocks.CONTROLLABLE_TRANSPARENT_STONE);
                        entries.add(RPBlocks.ITEM_PACKER);
                        entries.add(RPBlocks.ITEM_UNPACKER);
                        entries.add(RPBlocks.ITEM_CALIBRATOR);
                        entries.add(RPBlocks.ITEM_STEERING_GEAR);
                        entries.add(RPBlocks.ITEM_RESTARTING);
                        entries.add(RPBlocks.JUMP_ENHANCER);
                        entries.add(RPBlocks.TRACTOR);
                        entries.add(RPBlocks.LASER_GENERATOR);
                        entries.add(RPBlocks.ITEM_COLLECTOR_BLOCK);
                        entries.add(RPBlocks.ACTIVE_DEFENSE);
                        entries.add(RPBlocks.RESTRAINING_FORCE_BLOCK);
                        entries.add(RPBlocks.BIOLOGICAL_FIELD);
                        entries.add(RPBlocks.EXPLOSION_ABSORBER_BLOCK);
                        entries.add(RPBlocks.ELEVATOR_BLOCK);

                        entries.add(SMOKE_GRENADE);
                        entries.add(FLASH_GRENADE);
                    })
                    .build()
    );

    public static void registerItemsGroup() {
    }
}
