package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.item.RPItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class IncendiaryGrenadeEntity extends AbstractGrenadeEntity {
    private static final short EFFECT_DURATION = 6;
    private static final short EFFECT_SQUARED = EFFECT_DURATION * EFFECT_DURATION;

    public IncendiaryGrenadeEntity(EntityType<? extends IncendiaryGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public IncendiaryGrenadeEntity(World world, LivingEntity owner) {
        super(RPEntities.INCENDIARY_GRENADE_ENTITY_ENTITY, world, owner);
    }

    public IncendiaryGrenadeEntity(World world, double x, double y, double z) {
        super(RPEntities.INCENDIARY_GRENADE_ENTITY_ENTITY, world, x, y, z);
    }

    @Override
    protected void explode() {
        if (this.getWorld().isClient) return;
        ServerWorld world = (ServerWorld) this.getWorld();

        BlockPos center = this.getBlockPos();
        world.spawnParticles(ParticleTypes.LAVA, center.getX(), center.getY() + 1, center.getZ(),
                10, 0.2, 0.2, 0.2, 0.05);

        world.playSound(null, getBlockPos(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.5f);

        BlockPos.stream(center.add(-EFFECT_DURATION, -EFFECT_DURATION, -EFFECT_DURATION),
                        center.add(EFFECT_DURATION, EFFECT_DURATION, EFFECT_DURATION))
                .filter(pos -> center.getSquaredDistance(pos) <= EFFECT_SQUARED)
                .filter(pos -> world.getBlockState(pos).isAir())
                .forEach(pos -> world.setBlockState(pos, Blocks.FIRE.getDefaultState().with(FireBlock.AGE, 15)));

        Box explosionBox = new Box(center).expand(EFFECT_DURATION);
        world.getEntitiesByClass(Entity.class, explosionBox, entity -> entity instanceof LivingEntity)
                .forEach(entity -> entity.setOnFireFor(20));

        this.discard();
    }

    @Override
    protected int getDefaultFuse() {
        return 80;
    }

    @Override
    protected short getMaxBounces() {
        return 2;
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.INCENDIARY_GRENADE;
    }
}
