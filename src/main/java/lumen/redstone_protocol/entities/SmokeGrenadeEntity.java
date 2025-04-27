package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmokeGrenadeEntity extends AbstractGrenadeEntity {
    public SmokeGrenadeEntity(EntityType<? extends SmokeGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmokeGrenadeEntity(World world, LivingEntity owner) {
        super(RPEntities.SMOKE_GRENADE, world, owner);
    }

    public SmokeGrenadeEntity(World world, double x, double y, double z) {
        super(RPEntities.SMOKE_GRENADE, world, x, y, z);
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.SMOKE_GRENADE;
    }

    @Override
    protected void explode() {
        World world = this.getWorld();
        if (world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        BlockPos pos = this.getBlockPos();

        serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                pos.getX(), pos.getY(), pos.getZ(),
                1,
                0, 0, 0, 0);

        serverWorld.playSound(null, getBlockPos(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 0.8f, 1.0f);

        SmokeEffectAreaEntity effectArea = new SmokeEffectAreaEntity(world, pos, 8);
        serverWorld.spawnEntity(effectArea);
    }
}
