package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.HashMap;

public class TractorBlock extends Block {
    private static final int RANGE = 8;
    private static final double PULL_STRENGTH = 0.03f;
    private static final int TICK_DELAY = 2;
    private static final Vec3d ZERO_Y = new Vec3d(1, 0, 1);

    private static final HashMap<BlockPos, Box> SEARCH_BOX_MAP = new HashMap<>();

    public TractorBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) return;
        if (world.isReceivingRedstonePower(pos)) {
            initializeSearchBox(pos);
            world.scheduleBlockTick(pos, this, TICK_DELAY);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        SEARCH_BOX_MAP.remove(pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isReceivingRedstonePower(pos)) {
            moveEntities(world, pos);
            world.scheduleBlockTick(pos, this, TICK_DELAY);
        }
    }

    private void initializeSearchBox(BlockPos pos) {
        if (SEARCH_BOX_MAP.get(pos) == null) {
            SEARCH_BOX_MAP.put(pos, new Box(pos).expand(RANGE));
        }
    }

    private void moveEntities(ServerWorld world, BlockPos pos) {
        Box searchBox = SEARCH_BOX_MAP.get(pos);
        if (searchBox == null) {
            initializeSearchBox(pos);
            return;
        }

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
}


