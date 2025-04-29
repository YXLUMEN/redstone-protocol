package lumen.redstone_protocol.item;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RPItems {
    public static final Item SMOKE_GRENADE = register("smoke_grenade", new SmokeGrenadeItem(new Item.Settings().maxCount(32)));
    public static final Item FLASH_GRENADE = register("flash_grenade", new FlashGrenadeItem(new Item.Settings().maxCount(32)));
    public static final Item INCENDIARY_GRENADE = register("incendiary_grenade", new IncendiaryGrenadeItem(new Item.Settings().maxCount(32)));
    public static final Item FRAG_GRENADE = register("frag_grenade", new FragGrenadeItem(new Item.Settings().maxCount(32)));
    public static final Item HOWITZER_152 = register("howitzer152", new Howitzer152Item(new Item.Settings().maxCount(32)));

    public static <T extends Item> T register(String id, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(RedstoneProtocol.MOD_ID, id), item);
    }

    public static void registerItems() {
    }
}
