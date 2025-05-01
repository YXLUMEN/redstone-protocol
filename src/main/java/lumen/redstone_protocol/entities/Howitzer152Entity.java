package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import lumen.redstone_protocol.util.TrajectoryRayCaster;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static lumen.redstone_protocol.entities.FragGrenadeEntity.getRandomDirection;

public class Howitzer152Entity extends AbstractGrenadeEntity {
    private static final float POWER = 64;
    private static final short FRAG_COUNT = 256;

    public Howitzer152Entity(EntityType<? extends AbstractGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public Howitzer152Entity(World world, LivingEntity owner) {
        super(RPEntities.HOWITZER_152_ENTITY, world, owner);
    }

    public Howitzer152Entity(World world, double x, double y, double z) {
        super(RPEntities.HOWITZER_152_ENTITY, world, x, y, z);
    }

    @Override
    protected void explode() {
        World world = this.getWorld();

        world.createExplosion(this,
                this.getX(), this.getY(), this.getZ(),
                POWER,
                true,
                World.ExplosionSourceType.NONE
        );

        if (world instanceof ServerWorld serverWorld) {
            Vec3d start = this.getPos().add(0, 0.2, 0);
            for (int i = 0; i < FRAG_COUNT; i++) {
                Vec3d randomDir = getRandomDirection(this.random);
                TrajectoryRayCaster.rayCast(
                        serverWorld,
                        this,
                        start,
                        randomDir,
                        200.0f
                );
            }
        }

        this.discard();
    }

    @Override
    protected int getDefaultFuse() {
        return 20;
    }

    @Override
    protected short getMaxBounces() {
        return 1;
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.HOWITZER_152;
    }

    @Override
    protected boolean isInstant() {
        return true;
    }
}
