package lumen.redstone_protocol.effect;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class RPEffect {
    public static final RegistryEntry<StatusEffect> FLASHED =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(RedstoneProtocol.MOD_ID, "flashed"),
                    new FlashEffect());

    public static void registerEffects() {
    }
}
