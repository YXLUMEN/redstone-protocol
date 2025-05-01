package lumen.redstone_protocol.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
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

public class TrajectoryRayCaster {
    private static final float HARD_THRESHOLD = 0.8f;
    private static final short MAX_BOUNCES = 3;
    private static final short BASE_RAY_LENGTH = 32;
    private static final float BOUNCE_DAMAGE_LOSS = 0.15f;
    private static final float SCATTER_ANGLE = 0.2f;

    private static final float DAMAGE_INFLUENCE = 0.3f;
    private static final float BOUNCE_FACTOR = 1.0f;
    private static final float RANDOM_VARIATION = 0.2f;
    private static final float ENTITY_DETECT_RADIUS = 0.4f;

    public static void rayCast(World world, Entity owner, Vec3d start, Vec3d dir, float damage) {
        rayCast(world, owner, start, dir, damage, BOUNCE_FACTOR);
    }

    public static void rayCast(World world, Entity owner, Vec3d start, Vec3d dir, float damage, float bounceFactor) {
        if (world == null || owner == null) return;

        Vec3d currentPos = new Vec3d(start.x, start.y, start.z);
        Vec3d currentDir = dir.normalize();

        float remainingDamage = damage;
        int remainingBounces = MAX_BOUNCES;

        final Random random = owner.getRandom();

        while (remainingBounces-- > 0 && remainingDamage > 0.5f) {
            Vec3d endPos = currentPos.add(currentDir.multiply(BASE_RAY_LENGTH));

            EntityHitResult entityHit = ProjectileUtil.raycast(
                    owner, currentPos, endPos,
                    new Box(currentPos, endPos).expand(ENTITY_DETECT_RADIUS),
                    e -> !e.isSpectator() && e.isAlive() && e.canHit(),
                    BASE_RAY_LENGTH * BASE_RAY_LENGTH
            );

            if (entityHit != null) {
                applyDamage(world, entityHit.getEntity(), remainingDamage, owner);
//                debugDrawRay(world, currentPos, entityHit.getPos(), ParticleTypes.FLAME);
                return;
            }

            BlockHitResult blockHit = world.raycast(new RaycastContext(
                    currentPos, endPos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    owner
            ));

            if (blockHit.getType() != HitResult.Type.BLOCK) {
//                debugDrawRay(world, currentPos, endPos, ParticleTypes.FLAME);
                break;
            }
//            debugDrawRay(world, currentPos, blockHit.getPos(), ParticleTypes.FLAME);

            BlockState state = world.getBlockState(blockHit.getBlockPos());
            final float originHardness = state.getHardness(world, blockHit.getBlockPos());
            final float hardness = originHardness >= 0 ? originHardness : 100.0f;

            if (shouldBounce(hardness, remainingDamage, bounceFactor, random)) {
                // 反弹
                currentDir = calculateReflection(currentDir, blockHit.getSide(), random).normalize();
                currentPos = blockHit.getPos().add(currentDir.multiply(0.1));
                remainingDamage *= (1 - BOUNCE_DAMAGE_LOSS);

                spanHitParticles(world, currentPos, ParticleTypes.SMOKE);
            } else if (tryPenetrate(hardness, remainingDamage, random)) {
                // 穿透
                currentPos = blockHit.getPos().add(currentDir.multiply(0.5));
                remainingDamage *= Math.max(0.3f, 1 - (hardness * 0.1f));

                spanHitParticles(world, currentPos, ParticleTypes.CRIT);
            } else {
                break;
            }
        }
    }

    private static boolean shouldBounce(float hardness, float damage, float boundFactor, Random random) {
        float hardnessFactor = Math.min(1, hardness / HARD_THRESHOLD);
        float damageFactor = 1 / (1 + DAMAGE_INFLUENCE * damage);
        float randomVariation = 1 + (random.nextFloat() * 2 - 1) * RANDOM_VARIATION;

        float bounceProbability = (0.5f * (hardnessFactor + damageFactor)) * randomVariation * boundFactor;

        bounceProbability = MathHelper.clamp(bounceProbability, 0.1f, 0.8f);

        return random.nextFloat() < bounceProbability;
    }

    private static boolean tryPenetrate(float hardness, float damage, Random random) {
        return (damage * (0.7f + random.nextFloat() * 0.3f)) > hardness * 0.7f;
    }

    private static Vec3d calculateReflection(Vec3d incoming, Direction face, Random random) {
        Vec3d normal = new Vec3d(face.getOffsetX(), face.getOffsetY(), face.getOffsetZ());
        Vec3d reflected = incoming.subtract(normal.multiply(2 * incoming.dotProduct(normal)));

        float angle = random.nextFloat() * SCATTER_ANGLE;
        return reflected.rotateX(angle * (random.nextBoolean() ? 1 : -1))
                .rotateY(angle * (random.nextBoolean() ? 1 : -1));
    }

    private static void applyDamage(World world, Entity target, float damage, Entity owner) {
        if (target instanceof LivingEntity living) {
            living.hurtTime = 0;
            living.timeUntilRegen = 0;
        }
        if (owner instanceof LivingEntity livingOwner) {
            target.damage(world.getDamageSources().mobAttack(livingOwner), damage);
        } else {
            target.damage(world.getDamageSources().generic(), damage);
        }
    }

    private static void spanHitParticles(World world, Vec3d pos, SimpleParticleType particleType) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        serverWorld.spawnParticles(particleType, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
    }
}