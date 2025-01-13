package lumen.redstone_protocol.block;

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

public class TractorBlock extends Block {
    private static final int RANGE = 8;
    private static final double PULL_STRENGTH = 0.03f;
    private static final int TICK_DELAY = 2;
    private static final Vec3d ZERO_Y = new Vec3d(1, 0, 1);

    private Box searchBox;
    private Vec3d cachedBlockCenter;

    public TractorBlock(Settings settings) {
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

    private void initializeSearchBox(BlockPos pos) {
        if (searchBox == null) {
            searchBox = new Box(pos).expand(RANGE);
            cachedBlockCenter = Vec3d.ofCenter(pos, 0); // Cache the block center
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isReceivingRedstonePower(pos)) {
            moveEntities(world, pos);
            world.scheduleBlockTick(pos, this, TICK_DELAY);
        } else {
            searchBox = null;
        }
    }

    private void moveEntities(ServerWorld world, BlockPos pos) {
        if (searchBox == null) initializeSearchBox(pos);

        world.getEntitiesByClass(Entity.class, searchBox, this::shouldAffectEntity)
                .forEach(this::applyPullForce);
    }

    private boolean shouldAffectEntity(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            return !(player.isSneaking() || player.getAbilities().flying);
        }
        return true;
    }

    private void applyPullForce(Entity entity) {
        Vec3d entityPos = entity.getPos();
        Vec3d pullVector = cachedBlockCenter.subtract(entityPos)
                .multiply(ZERO_Y)
                .normalize()
                .multiply(PULL_STRENGTH);

        entity.addVelocity(pullVector.x, 0, pullVector.z);
        entity.velocityModified = true;
    }
}


