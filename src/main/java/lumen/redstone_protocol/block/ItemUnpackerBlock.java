package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUnpackerBlock extends CarpetBlock {
    public ItemUnpackerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof ItemEntity item) {
            if (item.removeCommandTag("rp_packed")) item.setToDefaultPickupDelay();
        }
        super.onEntityCollision(state, world, pos, entity);
    }
}
