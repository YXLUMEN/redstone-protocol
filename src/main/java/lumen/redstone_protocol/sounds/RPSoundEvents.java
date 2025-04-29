package lumen.redstone_protocol.sounds;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class RPSoundEvents {
    public static final SoundEvent BULLET_RICOCHET = registerSound("bullet_ricochet");
    public static final SoundEvent GAS = registerSound("gas");
    public static final SoundEvent PULL_BIN = registerSound("pull_bin");
    public static final SoundEvent THROW_GRENADE = registerSound("throw_grenade");
    public static final SoundEvent GRENADE_BOUNCE = registerSound("grenade_bounce");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(RedstoneProtocol.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerSounds() {
    }
}
