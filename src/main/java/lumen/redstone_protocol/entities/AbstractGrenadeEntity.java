package lumen.redstone_protocol.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractGrenadeEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(AbstractGrenadeEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private short bounceCount = 0;

    public AbstractGrenadeEntity(EntityType<? extends AbstractGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbstractGrenadeEntity(EntityType<? extends AbstractGrenadeEntity> entity, World world, LivingEntity owner) {
        super(entity, owner, world);
    }

    public AbstractGrenadeEntity(EntityType<? extends AbstractGrenadeEntity> entity, World world, double x, double y, double z) {
        super(entity, x, y, z, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (this.bounceCount >= getMaxBounces()) {
            this.setVelocity(Vec3d.ZERO);
            return;
        }

        Vec3d currentVelocity = this.getVelocity();
        Vec3d bounceVelocity = calculateBounceVelocity(blockHitResult, currentVelocity);

        this.setVelocity(bounceVelocity);
        this.bounceCount++;
    }

    @Override
    public void tick() {
        super.tick();

        int fuse = this.dataTracker.get(FUSE) - 1;
        this.dataTracker.set(FUSE, fuse);

        if (fuse <= 0) {
            this.explode();
            this.discard();
            return;
        }

        if (fuse <= 10) {
            this.getWorld().addParticle(ParticleTypes.FLAME,
                    this.getX(), this.getY() + 0.3, this.getZ(),
                    0.0D, 0.0D, 0.0D);
        }
        this.getWorld().addParticle(ParticleTypes.SMOKE,
                this.getX(), this.getY() + 0.3, this.getZ(),
                0.0D, 0.0D, 0.0D);
    }

    protected abstract void explode();

    private Vec3d calculateBounceVelocity(BlockHitResult hit, Vec3d currentVel) {
        Direction face = hit.getSide();
        Vec3d normal = new Vec3d(face.getOffsetX(), face.getOffsetY(), face.getOffsetZ());

        Vec3d reflected = currentVel.subtract(normal.multiply(2 * currentVel.dotProduct(normal)));
        return reflected.multiply(0.3d);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, getDefaultFuse());
    }

    protected abstract int getDefaultFuse();

    protected abstract short getMaxBounces();
}
