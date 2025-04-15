package lumen.redstone_protocol.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ActiveDefenseBlockEntity extends BlockEntity {
    private static final double DETECTION_RADIUS = 6.0d;

    private final Vec3d BLOCK_CENTER = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    private final Box detectionBox = new Box(pos).expand(DETECTION_RADIUS);

    public ActiveDefenseBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.ACTIVE_DEFENSE_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;

        List<ProjectileEntity> projectiles = world.getEntitiesByClass(ProjectileEntity.class, this.detectionBox, projectile -> {
            // 如果弹射物没有指定射手,则视为外部发射的
            if (projectile.getOwner() == null) {
                return true;
            }

            // 否则比较射手与防御系统中心的距离
            double distanceSquared = projectile.getOwner().getPos().squaredDistanceTo(BLOCK_CENTER);

            return distanceSquared > DETECTION_RADIUS * DETECTION_RADIUS;
        });

        for (ProjectileEntity projectile : projectiles) {
            if (world instanceof ServerWorld serverWorld) {
                // 在消除位置生成烟雾
                serverWorld.spawnParticles(ParticleTypes.SMOKE,
                        projectile.getX(), projectile.getY(), projectile.getZ(),
                        10, 0.2, 0.2, 0.2, 0.01);

                Vec3d projectilePos = projectile.getPos();

                // 沿着 ADS 中心与弹射物当前位置之间插值生成粒子
                Vec3d diff = projectilePos.subtract(BLOCK_CENTER);

                // 根据距离确定生成粒子的数量
                int steps = (int) (diff.length() / 0.5);
                if (steps < 1) steps = 1;
                for (int i = 0; i <= steps; i++) {
                    double fraction = i / (double) steps;
                    Vec3d point = BLOCK_CENTER.add(diff.multiply(fraction));
                    serverWorld.spawnParticles(ParticleTypes.END_ROD,
                            point.x, point.y, point.z,
                            1,
                            0, 0, 0, 0);
                }
            }

            projectile.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.6f, 1.0f);
            projectile.discard();
        }
    }
}
