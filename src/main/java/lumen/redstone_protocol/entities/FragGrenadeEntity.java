package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import lumen.redstone_protocol.util.FragmentRayCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FragGrenadeEntity extends AbstractGrenadeEntity {
    private static final float POWER = 3.0f;
    private static final short FRAG_COUNT = 36;

    public FragGrenadeEntity(EntityType<? extends AbstractGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FragGrenadeEntity(World world, LivingEntity owner) {
        super(RPEntities.FRAG_GRENADE_ENTITY, world, owner);
    }

    public FragGrenadeEntity(World world, double x, double y, double z) {
        super(RPEntities.FRAG_GRENADE_ENTITY, world, x, y, z);
    }

    @Override
    protected void explode() {
        World world = this.getWorld();

        world.createExplosion(this,
                this.getX(), this.getY(), this.getZ(),
                POWER,
                false,
                World.ExplosionSourceType.NONE
        );

        Entity owner = this.getOwner() == null ? this : this.getOwner();
        Vec3d start = this.getPos().add(0, 0.2, 0);

        for (int i = 0; i < FRAG_COUNT; i++) {
            Vec3d randomDir = getUniformRandomDirection(this.random);
            FragmentRayCaster.cast(
                    world,
                    start,
                    randomDir,
                    16.0f,
                    owner,
                    this
            );
        }

        this.discard();
    }

    public static Vec3d getUniformRandomDirection(Random random) {
        final double goldenRatio = (1 + Math.sqrt(5)) / 2;
        final double angleIncrement = Math.PI * 2 * goldenRatio;

        double y = 1 - (random.nextDouble() * 2); // y ∈ [-1, 1]
        double radius = Math.sqrt(1 - y * y);

        double theta = angleIncrement * random.nextDouble();

        return new Vec3d(
                Math.cos(theta) * radius,
                y,
                Math.sin(theta) * radius
        ).normalize();
    }

    @Override
    protected int getDefaultFuse() {
        return 60;
    }

    @Override
    protected short getMaxBounces() {
        return 3;
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.FRAG_GRENADE;
    }
}
