package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.RPProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class LaserGenEntity extends BlockEntity {
    private static final int MAX_MODE = RPProperties.LASER_MODE.getValues().size();
    private short currentMode = 0;

    public LaserGenEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.LASER_GEN_ENTITY, pos, state);
    }

    public void nextMode() {
        this.currentMode = (short) ((this.currentMode + 1) % MAX_MODE);
        markDirty();
    }

    public int getCurrentMode() {
        return this.currentMode;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putShort("currentMode", this.currentMode);
        return nbtCompound;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putShort("currentMode", this.currentMode);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.currentMode = nbt.getShort("currentMode");
    }
}
