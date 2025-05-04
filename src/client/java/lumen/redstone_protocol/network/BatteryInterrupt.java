package lumen.redstone_protocol.network;

import lumen.redstone_protocol.sounds.RPSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class BatteryInterrupt {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SoundInterruptS2CPayload.ID, (payload, context) -> {
            SoundEvent soundEvent = switch (payload.type()) {
                case 1 -> RPSoundEvents.CELL_CHARGE;
                case 2 -> RPSoundEvents.KIT_USING;
                case 3 -> RPSoundEvents.PHOENIX_KIT_CHARGE;
                default -> RPSoundEvents.BATTERY_CHARGE;
            };

            MinecraftClient.getInstance().getSoundManager().stopSounds(
                    soundEvent.getId(),
                    SoundCategory.PLAYERS);
        });
    }
}
