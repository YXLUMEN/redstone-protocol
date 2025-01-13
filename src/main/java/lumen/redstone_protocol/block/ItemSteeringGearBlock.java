package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemSteeringGearBlock extends CarpetBlock {
    public static final DirectionProperty HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    public static final DirectionProperty OUTPUT_FACING = DirectionProperty.of("output_facing", Direction.Type.HORIZONTAL);

    public ItemSteeringGearBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(HORIZONTAL_FACING, Direction.NORTH)
                .with(OUTPUT_FACING, Direction.NORTH));
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity.isSneaking()) return;

        int newX = pos.getX();
        int newY = pos.getY();
        int newZ = pos.getZ();

        switch (state.get(OUTPUT_FACING)) {
            case NORTH:
                newZ -= 1;
                break;
            case SOUTH:
                newZ += 1;
                break;
            case WEST:
                newX -= 1;
                break;
            case EAST:
                newX += 1;
                break;
        }

        entity.setPosition(newX + 0.5f, newY, newZ + 0.5f);

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.canModifyBlocks()) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                BlockState blockState = state.cycle(OUTPUT_FACING);
                world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
                return ActionResult.CONSUME;
            }
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing())
                .with(OUTPUT_FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, OUTPUT_FACING);
    }
}

