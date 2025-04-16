package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.mixin.PersistentProjectileEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ProjectileReducerBlockEntity extends BlockEntity {
    private static final float EFFECT_RADIUS = 12.0f;
    private static final short[][] OFFSETS = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};

    private final Box effectBox = new Box(pos).expand(EFFECT_RADIUS);

    public ProjectileReducerBlockEntity(BlockPos pos, BlockState state) {
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

        List<ProjectileEntity> projectiles = world.getEntitiesByClass(
                ProjectileEntity.class, this.effectBox, projectile -> {
                    if (projectile instanceof PersistentProjectileEntity) {
                        return !((PersistentProjectileEntityAccessor) projectile).getInGround();
                    }
                    return true;
                });

        for (ProjectileEntity projectile : projectiles) {
            if (world instanceof ServerWorld serverWorld) {
                Vec3d projectilePos = projectile.getPos();
                serverWorld.spawnParticles(
                        ParticleTypes.ITEM_SLIME,
                        projectilePos.x, projectilePos.y, projectilePos.z,
                        1,
                        0, 0, 0, 0);
            }
            projectile.setVelocity(projectile.getVelocity().multiply(0.2f, 0.5f, 0.2f));
            projectile.velocityModified = true;
        }
    }
}
