package lumen.redstone_protocol.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemCalibrator extends CarpetBlock {
    public ItemCalibrator(Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.getTime() % 2 != 0) return;
        if (entity.isSneaking()) return;

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY();
        double centerZ = pos.getZ() + 0.5;

        Vec3d currentPosition = entity.getPos();
        double speedFactor = 0.35f;

        double newX = currentPosition.x + (centerX - currentPosition.x) * speedFactor;
        double newY = currentPosition.y + (centerY - currentPosition.y) * speedFactor;
        double newZ = currentPosition.z + (centerZ - currentPosition.z) * speedFactor;

        entity.setPosition(newX, newY, newZ);

        super.onEntityCollision(state, world, pos, entity);
    }
}
