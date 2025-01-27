package lumen.redstone_protocol.block;

import com.mojang.serialization.MapCodec;
import lumen.redstone_protocol.RPProperties;
import lumen.redstone_protocol.block_entity.LaserGenEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserGeneratorBlock extends BlockWithEntity {
    public static final MapCodec<LaserGeneratorBlock> CODEC = createCodec(LaserGeneratorBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    private static final int RANGE = 15;
    private static final Direction[] DIRECTIONS = Direction.values();

    public LaserGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserGenEntity(pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) return;
        boolean powered = state.get(POWERED);

        if (powered == world.isReceivingRedstonePower(pos)) return;
        if (powered) {
            checkAndClear((ServerWorld) world, pos);
        } else {
            checkAndFill((ServerWorld) world, pos);
        }
        world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient && !state.isOf(newState.getBlock())) {
            checkAndClear((ServerWorld) world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private void checkAndFill(ServerWorld world, BlockPos pos) {
        int mode = 0;
        if (world.getBlockEntity(pos) instanceof LaserGenEntity laserGenEntity) {
            mode = laserGenEntity.getCurrentMode();
        }
        for (Direction direction : DIRECTIONS) {
            BlockPos endPos = findEndPos(world, pos, direction);

            if (endPos == null) continue;
            fillBetween(world, pos, endPos, direction, mode);
        }
    }

    /**
     * 搜索正交位置上的其它发生器
     *
     * @return 只会返回 LaserGeneratorBlock位置 或 null.
     */
    private @Nullable BlockPos findEndPos(ServerWorld world, BlockPos startPos, Direction direction) {
        BlockPos.Mutable currentPos = new BlockPos.Mutable().set(startPos);

        for (int i = 0; i < RANGE; i++) {
            currentPos.move(direction);

            BlockState state = world.getBlockState(currentPos);
            if (state.isOf(this)) return currentPos.toImmutable();
            if (!state.isReplaceable()) return null;
        }
        return null;
    }

    // 在两个发生器之间填充"激光"
    private void fillBetween(ServerWorld world, BlockPos startPos, BlockPos endPos, Direction direction, int mode) {
        BlockState forceState = RPBlocks.TRACTOR_FORCE.getDefaultState()
                .with(Properties.AXIS, direction.getAxis())
                .with(RPProperties.LASER_MODE, mode);

        List<BlockPos> positions = BlockPos.stream(startPos, endPos)
                .filter(pos -> world.getBlockState(pos).isReplaceable())
                .map(BlockPos::toImmutable)
                .toList();

        positions.forEach(pos -> world.setBlockState(pos, forceState));
    }

    // 检查并摧毁"激光"
    public static void checkAndClear(ServerWorld world, BlockPos pos) {
        for (Direction direction : DIRECTIONS) {
            BlockPos.Mutable checkPos = new BlockPos.Mutable().set(pos);

            for (int i = 1; i <= RANGE; i++) {
                checkPos.move(direction);

                BlockState state = world.getBlockState(checkPos);
                if (state.isAir() || !(state.getBlock() instanceof LaserBlock)) break;

                world.breakBlock(checkPos.toImmutable(), false);
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof LaserGenEntity laserGenEntity)) {
            return super.onUse(state, world, pos, player, hit);
        }
        laserGenEntity.nextMode();

        // switch laser mode
        if (state.get(POWERED) && !world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;
            checkAndClear(serverWorld, pos);
            checkAndFill(serverWorld, pos);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }
}

