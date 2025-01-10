package lumen.redstone_protocol.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SelectTransparentBlock extends TransparentBlock {
    private final Class<? extends Entity> allowedEntityClass;

    public SelectTransparentBlock(AbstractBlock.Settings settings, Class<? extends Entity> allowedEntityClass) {
        super(settings);
        this.allowedEntityClass = allowedEntityClass;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context instanceof EntityShapeContext) {
            Entity entity = ((EntityShapeContext) context).getEntity();
            if (allowedEntityClass.isInstance(entity)) {
                return VoxelShapes.empty();
            }
        }
        return super.getCollisionShape(state, world, pos, context);
    }
}

