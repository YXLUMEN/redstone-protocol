package lumen.redstone_protocol.network;

import lumen.redstone_protocol.render.FlashEffectRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FlashEffectClientPacket {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(FlashEffectS2CPayload.ID, (payload, context) -> {
            float strength = payload.strength();
            FlashEffectRenderer.handleFlashEffect(strength);
        });
    }
}
