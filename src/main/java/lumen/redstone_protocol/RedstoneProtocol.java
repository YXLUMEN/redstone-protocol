package lumen.redstone_protocol;

import lumen.redstone_protocol.block.RPBlocks;
import lumen.redstone_protocol.block_entity.RPBlockEntities;
import lumen.redstone_protocol.item.RPItems;
import lumen.redstone_protocol.screen_handler.RPScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstoneProtocol implements ModInitializer {
    public static final String MOD_ID = "redstone-protocol";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(), Identifier.of(RedstoneProtocol.MOD_ID, "item_group"));

    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(RPBlocks.LOW_FRICTION_GLASS))
            .displayName(Text.translatable("itemGroup.redstone-protocol"))
            .build();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Redstone Protocol");

        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

        RPItems.initialize();
        RPBlocks.initialize();
        RPBlockEntities.initialize();
        RPScreenHandler.initialize();
    }
}