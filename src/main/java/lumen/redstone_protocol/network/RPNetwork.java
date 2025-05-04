package lumen.redstone_protocol.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class RPNetwork {
    public static void registerNetwork() {
        PayloadTypeRegistry.playS2C().register(FlashEffectS2CPayload.ID, FlashEffectS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SoundInterruptS2CPayload.ID, SoundInterruptS2CPayload.CODEC);
    }
}
