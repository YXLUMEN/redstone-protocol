package lumen.redstone_protocol.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRestartingBlock extends CarpetBlock {
    public ItemRestartingBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof ItemEntity item) {
            item.age = 0;
        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
