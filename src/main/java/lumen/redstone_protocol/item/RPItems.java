package lumen.redstone_protocol.item;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RPItems {
    public static final Item SMOKE_BOMB = register("smoke_bomb", new SmokeBombItem(new Item.Settings().maxCount(32)));

    public static <T extends Item> T register(String id, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(RedstoneProtocol.MOD_ID, id), item);
    }

    public static void registerItems() {
    }
}
