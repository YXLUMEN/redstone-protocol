package lumen.redstone_protocol.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaserGeneratorBlock extends Block {
    private static final int RANGE = 15;
    private static final Direction[] DIRECTIONS = Direction.values();

    public LaserGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) checkAndFill((ServerWorld) world, pos);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient && !state.isOf(newState.getBlock())) {
            checkAndClear((ServerWorld) world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private void checkAndFill(ServerWorld world, BlockPos pos) {
        for (Direction direction : DIRECTIONS) {
            BlockPos endPos = findEndPos(world, pos, direction);
            if (endPos == null) continue;
            if (world.getBlockState(endPos).getBlock() instanceof LaserGeneratorBlock) {
                fillBetween(world, pos, endPos, direction);
                return;
            }
        }
    }

    private @Nullable BlockPos findEndPos(ServerWorld world, BlockPos startPos, Direction direction) {
        BlockPos.Mutable currentPos = new BlockPos.Mutable().set(startPos);
        for (int i = 0; i < RANGE; i++) {
            currentPos.move(direction);
            if (world.getBlockState(currentPos).isOf(this)) {
                return currentPos.toImmutable();
            }
            if (!world.getBlockState(currentPos).isAir()) {
                return null;
            }
        }
        return null;
    }

    private void fillBetween(ServerWorld world, BlockPos startPos, BlockPos endPos, Direction direction) {
        BlockState forceState = RPBlocks.TRACTOR_FORCE.getDefaultState()
                .with(Properties.AXIS, direction.getAxis());

        List<BlockPos> positions = BlockPos.stream(startPos, endPos)
                .filter(pos -> world.getBlockState(pos).isAir())
                .map(BlockPos::toImmutable)
                .toList();

        positions.forEach(pos -> world.setBlockState(pos, forceState));
    }

    private void checkAndClear(ServerWorld world, BlockPos pos) {
        for (Direction direction : DIRECTIONS) {
            BlockPos.Mutable checkPos = new BlockPos.Mutable().set(pos);
            for (int i = 1; i <= RANGE; i++) {
                checkPos.move(direction);
                BlockState state = world.getBlockState(checkPos);
                if (state.isAir()) break;
                if (!(state.getBlock() instanceof LaserBlock)) break;
                world.breakBlock(checkPos.toImmutable(), false);
            }
        }
    }
}

