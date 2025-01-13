package lumen.redstone_protocol.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VerticalBoostBlock extends TransparentBlock {
    private final Vec3d verticalForce;

    public VerticalBoostBlock(AbstractBlock.Settings settings, double boostStrength) {
        super(settings);
        this.verticalForce = new Vec3d(0, boostStrength, 0);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;
        if (entity instanceof PlayerEntity player) {
            if (player.isSneaking() || player.getAbilities().flying) return;
        }

        Vec3d currentVelocity = entity.getVelocity();
        entity.setVelocity(currentVelocity.x, this.verticalForce.y, currentVelocity.z);
        entity.velocityModified = true;

        super.onEntityCollision(state, world, pos, entity);
    }
}

