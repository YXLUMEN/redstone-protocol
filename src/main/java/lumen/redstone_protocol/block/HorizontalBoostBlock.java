package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class HorizontalBoostBlock extends CarpetBlock {
    public static final DirectionProperty HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    private final double boostStrength;

    public HorizontalBoostBlock(AbstractBlock.Settings settings, double boostStrength) {
        super(settings);
        this.boostStrength = boostStrength;
        setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;
        if (entity.isSneaking()) return;

        Vec3d horizontalForce = switch (state.get(HORIZONTAL_FACING)) {
            case SOUTH -> new Vec3d(0, 0, boostStrength);
            case WEST -> new Vec3d(-boostStrength, 0, 0);
            case EAST -> new Vec3d(boostStrength, 0, 0);
            default -> new Vec3d(0, 0, -boostStrength);
        };

        boostEntity(horizontalForce, entity);

        super.onEntityCollision(state, world, pos, entity);
    }

    protected void boostEntity(@NotNull Vec3d horizontalForce, @NotNull Entity entity) {
        entity.setVelocity(horizontalForce.x, entity.getVelocity().y, horizontalForce.z);
        entity.velocityModified = true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }
}


