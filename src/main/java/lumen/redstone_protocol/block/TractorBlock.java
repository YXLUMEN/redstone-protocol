package lumen.redstone_protocol.block;

import com.mojang.serialization.MapCodec;
import lumen.redstone_protocol.block_entity.TractorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TractorBlock extends BlockWithEntity {
    public static final MapCodec<TractorBlock> CODEC = createCodec(TractorBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    private static final int RANGE = 8;
    private static final double PULL_STRENGTH = 0.03f;
    private static final Vec3d ZERO_Y = new Vec3d(1, 0.1, 1);
    private static final int TICK_DELAY = 2;

    public TractorBlock(Settings settings) {
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
        return new TractorBlockEntity(pos, state);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world.getBlockEntity(pos) instanceof TractorBlockEntity tractorBlockEntity) {
            tractorBlockEntity.setTrackBox(new Box(pos).expand(RANGE));
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) return;
        boolean powered = state.get(POWERED);

        if (powered == world.isReceivingRedstonePower(pos)) return;

        if (!powered) world.scheduleBlockTick(pos, this, TICK_DELAY);
        world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isReceivingRedstonePower(pos)) {
            moveEntities(world, pos);
            world.scheduleBlockTick(pos, this, TICK_DELAY);
        }
    }

    private void moveEntities(ServerWorld world, BlockPos pos) {
        Box searchBox = world.getBlockEntity(pos) instanceof TractorBlockEntity tractorBlockEntity ?
                tractorBlockEntity.getTrackBox() : new Box(pos).expand(RANGE);

        world.getEntitiesByClass(Entity.class, searchBox, this::shouldAffectEntity)
                .forEach(entity -> this.applyPullForce(entity, pos));
    }

    private boolean shouldAffectEntity(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            return !(player.isSneaking() || player.getAbilities().flying);
        }
        return true;
    }

    private void applyPullForce(Entity entity, BlockPos pos) {
        Vec3d entityPos = entity.getPos();
        Vec3d pullVector = pos.toCenterPos().subtract(entityPos)
                .multiply(ZERO_Y)
                .normalize()
                .multiply(PULL_STRENGTH);

        entity.addVelocity(pullVector.x, 0, pullVector.z);
        entity.velocityModified = true;
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


