package lumen.redstone_protocol.block;

import com.mojang.serialization.MapCodec;
import lumen.redstone_protocol.block_entity.ActiveDefenseBlockEntity;
import lumen.redstone_protocol.block_entity.RPBlockEntities;
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

public class ActiveDefenseBlock extends BlockWithEntity {
    public static final MapCodec<ActiveDefenseBlock> CODEC = createCodec(ActiveDefenseBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    public ActiveDefenseBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ActiveDefenseBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!state.get(POWERED)) return null;
        return validateTicker(type, RPBlockEntities.ACTIVE_DEFENSE_BLOCK_ENTITY, ActiveDefenseBlockEntity::tick);
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

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
