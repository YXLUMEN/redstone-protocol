package lumen.redstone_protocol.network;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SoundInterruptS2CPayload(int type) implements CustomPayload {
    public static final Identifier BATTERY_INTERRUPT_ID = Identifier.of(RedstoneProtocol.MOD_ID, "battery_interruption");
    public static final Id<SoundInterruptS2CPayload> ID = new CustomPayload.Id<>(BATTERY_INTERRUPT_ID);
    public static final PacketCodec<PacketByteBuf, SoundInterruptS2CPayload> CODEC = PacketCodec.of((value, buf) ->
            buf.writeInt(value.type), buf -> new SoundInterruptS2CPayload(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
