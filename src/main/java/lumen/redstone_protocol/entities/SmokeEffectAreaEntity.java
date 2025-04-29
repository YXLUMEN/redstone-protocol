package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.effect.RPEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SmokeEffectAreaEntity extends Entity {
    private static final int PHASE1_DURATION = 20;
    private static final int PHASE2_DURATION = 300;
    private static final int PHASE3_DURATION = 2000;
    private static final int BASE_DURATION = PHASE1_DURATION + PHASE2_DURATION + PHASE3_DURATION;

    private static final int SPREAD_INTERVAL = 5;

    private static final float DIFFUSION_RATE = 0.25f;
    private static final float MIN_DENSITY = 0.006f;
    private static final float DIFFUSION_COST_FACTOR = 1.1f;

    private BlockPos origin;
    private int maxRadius;
    private int age = 0;
    private int nextSpreadTick = 0;

    private Map<BlockPos, Float> smokeDensity = new HashMap<>();

    public SmokeEffectAreaEntity(EntityType<? extends SmokeEffectAreaEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmokeEffectAreaEntity(World world, BlockPos origin, int radius) {
        super(RPEntities.SMOKE_EFFECT_AREA, world);
        this.origin = origin;
        this.maxRadius = radius;
        this.setPosition(origin.getX(), origin.getY(), origin.getZ());

        smokeDensity.put(origin, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;
        if (this.age >= getAdjustedDuration()) {
            this.discard();
            return;
        }

        World world = this.getWorld();
        if (this.getWorld().isClient) return;
        if (++this.nextSpreadTick < SPREAD_INTERVAL) return;

        ServerWorld serverWorld = (ServerWorld) world;

        if (smokeDensity.isEmpty()) {
            serverWorld.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1.0f);
            this.discard();
            return;
        }

        final float diffusionFactor;
        if (this.age < PHASE1_DURATION) {
            diffusionFactor = 5.0f;
        } else if (this.age < PHASE2_DURATION) {
            diffusionFactor = 1.0f;
            smokeDensity.put(origin, 0.8f);
        } else {
            diffusionFactor = 0.8f;
        }

        diffuseSmoke(serverWorld, diffusionFactor);
        smokeEffect(serverWorld);
        this.nextSpreadTick = 0;
    }

    private void diffuseSmoke(ServerWorld world, float factor) {
        Map<BlockPos, Float> newDensity = new HashMap<>(this.smokeDensity);
        for (Map.Entry<BlockPos, Float> entry : new HashMap<>(this.smokeDensity).entrySet()) {
            BlockPos pos = entry.getKey();
            float currentDensity = entry.getValue();

            if (currentDensity <= 0) continue;

            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.offset(direction);
                if (getDistance(origin, neighbor) > maxRadius) continue;
                if (!canSmokePassThrough(world, neighbor)) continue;

                float neighborDensity = newDensity.getOrDefault(neighbor, 0.0f);
                float flux = (currentDensity - neighborDensity) * DIFFUSION_RATE * factor;
                flux = MathHelper.clamp(flux, 0f, currentDensity);

                newDensity.put(pos, Math.max(newDensity.get(pos) - flux * DIFFUSION_COST_FACTOR, 0.0f));
                newDensity.put(neighbor, Math.min(neighborDensity + flux, 1.0f));
            }
        }

        newDensity.values().removeIf(v -> v < MIN_DENSITY);
        this.smokeDensity = newDensity;
    }

    private static boolean canSmokePassThrough(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.isReplaceable();
    }

    private void smokeEffect(ServerWorld world) {
        for (Map.Entry<BlockPos, Float> entry : this.smokeDensity.entrySet()) {
            BlockPos pos = entry.getKey();

            spawnParticles(world, pos, entry.getValue(), this.random);
            extinguishFires(world, pos);
            blindEntity(world, pos);
        }
    }

    private static void extinguishFires(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.FIRE) || state.isOf(Blocks.SOUL_FIRE)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
        Box box = new Box(pos).expand(0.5);
        world.getEntitiesByClass(LivingEntity.class, box, Entity::isOnFire).forEach(Entity::extinguish);
    }

    private static void blindEntity(ServerWorld world, BlockPos pos) {
        Box effectBox = new Box(pos).expand(0.75);
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, effectBox, livingEntity -> true)) {
            if ((entity instanceof PlayerEntity player) && (player.isSpectator() || player.isCreative())) {
                continue;
            }

            if (entity instanceof MobEntity mob) {
                mob.setTarget(null);
                mob.setAttacking(false);
            }

            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,
                    40, 0, false, false, true));

            entity.addStatusEffect(new StatusEffectInstance(RPEffects.SMOKE_CLOAK,
                    40, 0, false, false, false));
        }
    }

    private static void spawnParticles(ServerWorld world, BlockPos pos, float density, Random random) {
        int count = MathHelper.ceil(density * 4);

        world.spawnParticles(ParticleTypes.LARGE_SMOKE,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                count,
                0.1 + random.nextFloat(), 0.1 + random.nextFloat(), 0.1 + random.nextFloat(),
                0.01);
    }

    private static double getDistance(BlockPos a, BlockPos b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) +
                Math.pow(a.getY() - b.getY(), 2) +
                Math.pow(a.getZ() - b.getZ(), 2));
    }

    private int getAdjustedDuration() {
        return BASE_DURATION;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }
}
