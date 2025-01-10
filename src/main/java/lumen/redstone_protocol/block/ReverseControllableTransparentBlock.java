package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ReverseControllableTransparentBlock extends ControllableTransparentBlock {
    public ReverseControllableTransparentBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(POWERED)) {
            return VoxelShapes.fullCube();
        }
        return VoxelShapes.empty();
    }
}
