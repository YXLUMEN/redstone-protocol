package lumen.redstone_protocol.block;

import com.mojang.serialization.MapCodec;
import lumen.redstone_protocol.block_entity.ProjectileReducerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectileReducerBlock extends BlockWithEntity {
    public static final MapCodec<ProjectileReducerBlock> CODEC = createCodec(ProjectileReducerBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    public ProjectileReducerBlock(Settings settings) {
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

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ProjectileReducerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!state.get(POWERED)) return null;
        return (worldIn, pos, stateIn, blockEntity) -> {
            if (blockEntity instanceof ProjectileReducerBlockEntity pRBlockEntity) {
                pRBlockEntity.tick();
            }
        };
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) return;

        boolean powered = state.get(POWERED);
        if (powered == world.isReceivingRedstonePower(pos)) return;

        world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
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
