package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class FragGrenadeEntity extends AbstractGrenadeEntity {
    private static final float POWER = 3.0f;
    private static final short FRAG_COUNT = 36;

    public FragGrenadeEntity(EntityType<? extends AbstractGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FragGrenadeEntity(World world, LivingEntity owner) {
        super(RPEntities.FRAG_GRENADE_ENTITY_ENTITY, world, owner);
    }

    public FragGrenadeEntity(World world, double x, double y, double z) {
        super(RPEntities.FRAG_GRENADE_ENTITY_ENTITY, world, x, y, z);
    }

    @Override
    protected void explode() {
        World world = this.getWorld();

        world.createExplosion(this,
                Explosion.createDamageSource(this.getWorld(), this),
                null,
                this.getX(), this.getY(), this.getZ(),
                POWER,
                false,
                World.ExplosionSourceType.NONE,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.ENTITY_GENERIC_EXPLODE
        );

        for (int i = 0; i < FRAG_COUNT; i++) {
            FragmentEntity fragment = new FragmentEntity(this.getOwner(), world, this.getX(), this.getY(), this.getZ());
            fragment.setInvulnerable(true);

            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double speed = 0.5 + random.nextDouble() * 1.5;

            fragment.setVelocity(
                    Math.sin(phi) * Math.cos(theta) * speed,
                    Math.cos(phi) * speed,
                    Math.sin(phi) * Math.sin(theta) * speed
            );

            world.spawnEntity(fragment);
        }

        this.discard();
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
