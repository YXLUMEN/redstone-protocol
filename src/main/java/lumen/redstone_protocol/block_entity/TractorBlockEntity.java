package lumen.redstone_protocol.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;


public class TractorBlockEntity extends BlockEntity {
    private Box TrackBox;

    public TractorBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.TRACTOR_ENTITY, pos, state);
    }

    public void setTrackBox(Box box) {
        if (this.TrackBox != null) return;

        this.TrackBox = box;
        markDirty();
    }

    public Box getTrackBox() {
        return this.TrackBox;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (this.TrackBox != null) {
            nbt.putIntArray("track_box", new int[]{
                    (int) this.TrackBox.getLengthX(),
                    (int) this.TrackBox.getLengthY(),
                    (int) this.TrackBox.getLengthZ(),
            });
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("track_box")) {
            int[] box = nbt.getIntArray("track_box");
            this.TrackBox = new Box(
                    this.pos.getX() - box[0],
                    this.pos.getY() - box[1],
                    this.pos.getZ() - box[2],
                    this.pos.getX() + box[0],
                    this.pos.getY() + box[1],
                    this.pos.getZ() + box[2]
            );
        }
    }
}
