package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SmokeBombEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(SmokeBombEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int DEFAULT_FUSE = 60;
    private static final short MAX_BOUNCES = 2;

    private short bounceCount = 0;

    public SmokeBombEntity(EntityType<? extends SmokeBombEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmokeBombEntity(World world, LivingEntity owner) {
        super(RPEntities.SMOKE_BOMB, owner, world);
    }

    public SmokeBombEntity(World world, double x, double y, double z) {
        super(RPEntities.SMOKE_BOMB, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.SMOKE_BOMB;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (this.bounceCount >= MAX_BOUNCES) {
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
        }
    }

    private void explode() {
        ServerWorld world = (ServerWorld) this.getWorld();
        BlockPos pos = this.getBlockPos();

        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                pos.getX(), pos.getY(), pos.getZ(),
                1,
                0, 0, 0, 0);

        world.playSound(null, getBlockPos(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 0.8f, 1.0f);

        SmokeEffectAreaEntity effectArea = new SmokeEffectAreaEntity(world, pos, 8);
        world.spawnEntity(effectArea);
    }

    private Vec3d calculateBounceVelocity(BlockHitResult hit, Vec3d currentVel) {
        Direction face = hit.getSide();
        Vec3d normal = new Vec3d(face.getOffsetX(), face.getOffsetY(), face.getOffsetZ());

        Vec3d reflected = currentVel.subtract(normal.multiply(2 * currentVel.dotProduct(normal)));
        return reflected.multiply(0.2d);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FUSE, DEFAULT_FUSE);
    }
}
