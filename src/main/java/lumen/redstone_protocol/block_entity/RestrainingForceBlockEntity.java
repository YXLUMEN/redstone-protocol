package lumen.redstone_protocol.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class RestrainingForceBlockEntity extends BlockEntity {
    private static final float EFFECT_RADIUS = 12.0f;
    private static final short[][] OFFSETS = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};

    private final Box effectBox = new Box(pos).expand(EFFECT_RADIUS);

    public RestrainingForceBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.PROJECTILE_REDUCER_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;

        if (world instanceof ServerWorld serverWorld) {
            for (short[] offset : OFFSETS) {
                serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                        pos.getX() + offset[0], pos.getY() + 1, pos.getZ() + offset[1],
                        1,
                        0, 0, 0, 0.0);
            }
        }

        List<Entity> entities = world.getEntitiesByClass(
                Entity.class, this.effectBox, entity -> !(entity instanceof PlayerEntity));

        for (Entity entity : entities) {
            if (world instanceof ServerWorld serverWorld) {
                Vec3d entityPos = entity.getPos();
                serverWorld.spawnParticles(
                        ParticleTypes.ITEM_SLIME,
                        entityPos.x, entityPos.y, entityPos.z,
                        1,
                        0, 0, 0, 0);
            }

            entity.setVelocity(entity.getVelocity().multiply(0.1f, 0.5f, 0.1f));
            entity.velocityModified = true;
        }
    }
}
