package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.mixin.PersistentProjectileEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ActiveDefenseBlockEntity extends BlockEntity {
    private static final short DETECTION_RADIUS = 6;
    private static final short DESTROY_COST = 12;
    private static final short MAX_DESTROY_COUNT = 60;

    private final Vec3d BLOCK_CENTER = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    private final Box detectionBox = new Box(pos).expand(DETECTION_RADIUS);

    private short destroyCount = 0;
    private boolean cooldown = false;

    public ActiveDefenseBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.ACTIVE_DEFENSE_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ActiveDefenseBlockEntity blockEntity) {
        if (world == null || world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        if (blockEntity.destroyCount > 0) blockEntity.destroyCount--;
        if (blockEntity.cooldown) {
            if (blockEntity.destroyCount == 0) blockEntity.cooldown = false;
            coolingDown(serverWorld, blockPos);
            return;
        }

        if (blockEntity.destroyCount >= MAX_DESTROY_COUNT) blockEntity.cooldown = true;

        // 击毁弹射物
        List<ProjectileEntity> projectiles = serverWorld.getEntitiesByClass(ProjectileEntity.class, blockEntity.detectionBox, projectile -> {
            if (projectile instanceof PersistentProjectileEntity) {
                if (((PersistentProjectileEntityAccessor) projectile).getInGround()) return false;
            }

            // 如果弹射物没有指定射手,则视为外部发射的
            if (projectile.getOwner() == null) return true;

            // 否则比较射手与防御系统中心的距离
            double distanceSquared = projectile.getOwner().getPos().squaredDistanceTo(blockEntity.BLOCK_CENTER);
            return distanceSquared > DETECTION_RADIUS * DETECTION_RADIUS;
        });

        for (ProjectileEntity projectile : projectiles) {
            if (projectile == null) continue;

            Vec3d projectilePos = projectile.getPos();
            spanDestroyParticles(serverWorld, projectilePos, blockEntity.BLOCK_CENTER);

            projectile.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.2f, 1.2f);
            projectile.discard();

            blockEntity.destroyCount += DESTROY_COST;
        }
    }

    private static void spanDestroyParticles(ServerWorld serverWorld, Vec3d pos, Vec3d blockCenter) {
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

        // 生成发射粒子
        Vec3d shout = blockCenter.add(diff.multiply(0.1));
        Vec3d direction = shout.normalize();

        serverWorld.spawnParticles(ParticleTypes.FLAME,
                shout.x, shout.y, shout.z,
                10, direction.x, 0, direction.z, 0.04);

        // 生成销毁粒子
        serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                pos.x, pos.y, pos.z,
                1, 0, 0, 0, 0);
    }

    private static void coolingDown(ServerWorld serverWorld, BlockPos pos) {
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                1, 0.2, 0.2, 0.2, 0.01
        );

        serverWorld.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
    }
}
