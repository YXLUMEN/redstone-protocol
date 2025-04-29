package lumen.redstone_protocol.util;

import lumen.redstone_protocol.entities.AbstractGrenadeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class FragmentRayCaster {
    private static final float HARD_THRESHOLD = 0.6f;
    private static final short MAX_BOUNCES = 3;
    private static final short RAY_LENGTH = 30;
    private static final float DAMAGE_LOSS = 0.15f;
    private static final float SCATTER_ANGLE = 0.2f;

    private static final float MIN_BOUNCE_CHANCE = 0.1f;
    private static final float MAX_BOUNCE_CHANCE = 0.9f;
    private static final float DAMAGE_INFLUENCE = 0.3f;
    private static final float RANDOM_VARIATION = 0.2f;

    public static void cast(World world, Vec3d start, Vec3d dir, float damage, Entity owner, AbstractGrenadeEntity grenade) {
        Vec3d pos = new Vec3d(start.x, start.y, start.z);
        Vec3d rayDir = dir.normalize();
        float remainingDamage = damage;
        int bounces = MAX_BOUNCES;
        Random random = owner.getRandom();

        while (bounces-- >= 0 && remainingDamage > 0.5f) {
            Vec3d end = pos.add(rayDir.multiply(RAY_LENGTH));

            EntityHitResult entityHit = ProjectileUtil.raycast(
                    grenade, pos, end,
                    new Box(pos, end).expand(0.4),
                    e -> !e.isSpectator() && e.isAlive() && e.canHit(),
                    0.4f
            );

            if (entityHit != null) {
                applyDamage(world, entityHit.getEntity(), remainingDamage, owner);
                return;
            }

            BlockHitResult blockHit = world.raycast(new RaycastContext(
                    pos, end,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    owner
            ));

            if (blockHit.getType() != HitResult.Type.BLOCK) break;

            BlockState state = world.getBlockState(blockHit.getBlockPos());
            float hardness = state.getBlock().getHardness();

            if (shouldBounce(hardness, remainingDamage, random)) {
                // 反弹
                rayDir = calculateReflection(rayDir, blockHit.getSide(), random);
                pos = blockHit.getPos().add(rayDir.multiply(0.1));
                remainingDamage *= (1 - DAMAGE_LOSS);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                }
            } else if (!tryPenetrate(hardness, remainingDamage, random)) {
                // 吸收
                break;
            } else {
                // 穿透
                pos = blockHit.getPos().add(rayDir.multiply(0.5));
                remainingDamage = Math.max(0.3f, 1 - hardness / 10f);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private static void applyDamage(World world, Entity target, float damage, Entity owner) {
        if (target instanceof LivingEntity living) {
            living.hurtTime = 0;
            living.timeUntilRegen = 0;
        }
        if (owner instanceof LivingEntity livingOwner) {
            target.damage(world.getDamageSources().mobAttackNoAggro(livingOwner), damage);
        } else {
            target.damage(world.getDamageSources().generic(), damage);
        }
    }

    private static boolean shouldBounce(float hardness, float damage, Random random) {
        // 计算硬度影响 (0-1)
        float hardnessFactor = Math.min(1, hardness / HARD_THRESHOLD);

        // 计算伤害影响 (高伤害降低反弹几率)
        float damageFactor = 1 / (1 + DAMAGE_INFLUENCE * damage);

        // 随机波动 (±20%)
        float randomVariation = 1 + (random.nextFloat() * 2 - 1) * RANDOM_VARIATION;

        float bounceProbability = (hardnessFactor * 0.5f +
                damageFactor * (1 - 0.5f)) *
                randomVariation;

        bounceProbability = MathHelper.clamp(bounceProbability, MIN_BOUNCE_CHANCE, MAX_BOUNCE_CHANCE);

        return random.nextFloat() < bounceProbability;
    }

    private static boolean tryPenetrate(float hardness, float damage, Random random) {
        return random.nextFloat() < Math.min(1, damage / (hardness + 1));
    }

    private static Vec3d calculateReflection(Vec3d incoming, Direction face, Random random) {
        Vec3d normal = new Vec3d(face.getOffsetX(), face.getOffsetY(), face.getOffsetZ());
        Vec3d reflected = incoming.subtract(normal.multiply(2 * incoming.dotProduct(normal)));

        float angle = random.nextFloat() * SCATTER_ANGLE;
        return reflected.rotateX(angle * (random.nextBoolean() ? 1 : -1))
                .rotateY(angle * (random.nextBoolean() ? 1 : -1));
    }
}