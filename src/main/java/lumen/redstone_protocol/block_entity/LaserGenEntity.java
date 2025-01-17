package lumen.redstone_protocol.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class LaserGenEntity extends BlockEntity {
    private int currentMode;

    public LaserGenEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.LASER_GEN_ENTITY, pos, state);
    }


    public void nextMode() {
        currentMode = (currentMode + 1) % 4;
        markDirty();
    }

    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("currentMode", currentMode);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        currentMode = nbt.getInt("currentMode");
    }
}
