package lumen.redstone_protocol.effect;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class RPEffects {
    public static final RegistryEntry<StatusEffect> LOW_FRICTION;

    static {
        LOW_FRICTION = Registry.registerReference(
                Registries.STATUS_EFFECT, Identifier.of(RedstoneProtocol.MOD_ID, "low_friction"), new LowFriction());
    }

    public static void initialize() {
    }
}
