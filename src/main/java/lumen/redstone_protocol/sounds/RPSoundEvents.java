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

    public static final SoundEvent PHOENIX_KIT_CHARGE = registerSound("phoenix_kit_charge");
    public static final SoundEvent BATTERY_CHARGE = registerSound("battery_charge");
    public static final SoundEvent CELL_CHARGE = registerSound("cell_charge");
    public static final SoundEvent BATTERY_CHARGE_START = registerSound("battery_charge_start");
    public static final SoundEvent BATTERY_CHARGE_FAIL = registerSound("battery_charge_fail");
    public static final SoundEvent BATTERY_CHARGE_FINISH_ENERGY = registerSound("battery_charge_finish_en");
    public static final SoundEvent BATTERY_CHARGE_FINISH_MEC = registerSound("battery_charge_finish_mec");

    public static final SoundEvent KIT_START = registerSound("kit_start");
    public static final SoundEvent KIT_USING = registerSound("kit_using");
    public static final SoundEvent KIT_FINISH = registerSound("kit_finish");
    public static final SoundEvent KIT_FAIL = registerSound("kit_fail");

    public static final SoundEvent SHIELD_CRASH_1 = registerSound("shield_crash_1");
    public static final SoundEvent SHIELD_CRASH_2 = registerSound("shield_crash_2");
    public static final SoundEvent SHIELD_CRASH_3 = registerSound("shield_crash_3");
    public static final SoundEvent SHIELD_CRASH_4 = registerSound("shield_crash_4");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(RedstoneProtocol.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerSounds() {
    }
}
