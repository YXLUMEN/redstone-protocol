package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.mixin.PersistentProjectileEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ActiveDefenseBlockEntity extends BlockEntity {
    private static final float DETECTION_RADIUS = 6.0f;

    private final Vec3d BLOCK_CENTER = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    private final Box detectionBox = new Box(pos).expand(DETECTION_RADIUS);

    public ActiveDefenseBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.ACTIVE_DEFENSE_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;

        // 击毁弹射物
        List<ProjectileEntity> projectiles = world.getEntitiesByClass(ProjectileEntity.class, this.detectionBox, projectile -> {
            if (projectile instanceof PersistentProjectileEntity) {
                if (((PersistentProjectileEntityAccessor) projectile).getInGround()) return false;
            }

            // 如果弹射物没有指定射手,则视为外部发射的
            if (projectile.getOwner() == null) return true;

            // 否则比较射手与防御系统中心的距离
            double distanceSquared = projectile.getOwner().getPos().squaredDistanceTo(BLOCK_CENTER);
            return distanceSquared > DETECTION_RADIUS * DETECTION_RADIUS;
        });

        for (ProjectileEntity projectile : projectiles) {
            if (world instanceof ServerWorld serverWorld) {
                Vec3d projectilePos = projectile.getPos();
                spanDestroyParticles(serverWorld, projectilePos, BLOCK_CENTER, 10);
            }

            projectile.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.5f, 1.2f);
            projectile.discard();
        }

        // 击毁TNT
        List<TntEntity> tntEntities = world.getEntitiesByClass(TntEntity.class, detectionBox, tnt -> true);
        for (TntEntity tnt : tntEntities) {
            if (world instanceof ServerWorld serverWorld) {
                Vec3d tntPos = tnt.getPos();
                spanDestroyParticles(serverWorld, tntPos, BLOCK_CENTER, 20);
            }

            tnt.playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
            tnt.discard();
        }
    }

    public static void spanDestroyParticles(ServerWorld serverWorld, Vec3d pos, Vec3d blockCenter, int particleCount) {
        // 沿着 ADS 中心与弹射物当前位置之间插值生成粒子
        Vec3d diff = pos.subtract(blockCenter);

        // 根据距离确定生成粒子的数量
        int steps = (int) (diff.length() / 0.3);
        if (steps < 1) steps = 1;
        for (int i = 0; i <= steps; i++) {
            double fraction = i / (double) steps;
            Vec3d point = blockCenter.add(diff.multiply(fraction));
            serverWorld.spawnParticles(ParticleTypes.END_ROD,
                    point.x, point.y, point.z,
                    1,
                    0, 0, 0, 0);
        }

        // 生成销毁粒子
        serverWorld.spawnParticles(ParticleTypes.SMOKE,
                pos.getX(), pos.getY(), pos.getZ(),
                particleCount, 0.2, 0.2, 0.2, 0.01);
    }
}
