package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.effect.RPEffect;
import lumen.redstone_protocol.item.RPItems;
import lumen.redstone_protocol.network.FlashEffectS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class FlashGrenadeEntity extends AbstractGrenadeEntity {
    public static final short FLASH_RANGE = 16;
    public static final float MAX_ANGLE = 120f;

    public FlashGrenadeEntity(EntityType<? extends FlashGrenadeEntity> entityType, World world) {
        super(entityType, world);
    }

    public FlashGrenadeEntity(World world, LivingEntity owner) {
        super(RPEntities.FLASH_GRENADE, world, owner);
    }

    public FlashGrenadeEntity(World world, double x, double y, double z) {
        super(RPEntities.FLASH_GRENADE, world, x, y, z);
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.FLASH_GRENADE;
    }

    @Override
    protected void explode() {
        if (this.getWorld().isClient) return;
        ServerWorld world = (ServerWorld) this.getWorld();

        Vec3d pos = this.getPos();
        world.spawnParticles(ParticleTypes.END_ROD, pos.x, pos.y + 1, pos.z,
                10, 0.2, 0.2, 0.2, 0.05);
        world.playSound(null, getBlockPos(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.5f);

        Vec3d flashPos = this.getPos().add(0, this.getHeight() / 2, 0);

        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(FLASH_RANGE),
                e -> e.squaredDistanceTo(flashPos) <= FLASH_RANGE * FLASH_RANGE);
        for (LivingEntity entity : entities) {
            if (entity == null) continue;

            if (entity instanceof PlayerEntity player) {
                final float impact = calculateFlashStrength(world, this.getPos(), player);
                if (impact <= 0.1f) continue;

                applyPlayerFlash((ServerPlayerEntity) player, impact);
                if (this.getOwner() instanceof PlayerEntity pOwner) {
                    player.damage(player.getWorld().getDamageSources().playerAttack(pOwner), 0.1f);
                }
                continue;
            }

            if (this.getOwner() instanceof PlayerEntity owner) {
                entity.damage(world.getDamageSources().playerAttack(owner), 0.1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(
                    RPEffect.FLASHED, 200, 0, true, true
            ));
        }
    }

    private static boolean isBlocked(ServerWorld world, Vec3d flashPos, PlayerEntity player) {
        RaycastContext context = new RaycastContext(
                player.getEyePos(), flashPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        );

        return world.raycast(context).getType() == HitResult.Type.BLOCK && world.getBlockState(world.raycast(context).getBlockPos()).isOpaque();
    }

    private static float calculateSusceptibility(PlayerEntity player) {
        float factor = 1f;
        if (player.isSubmergedInWater()) factor *= 0.8f;
        if (player.hasStatusEffect(RPEffect.FLASHED)) factor *= 1.1f;
        if (player.hasStatusEffect(StatusEffects.NIGHT_VISION)) factor *= 1.3f;
        if (player.hasStatusEffect(StatusEffects.BLINDNESS)) factor *= 0.6f;
        return factor;
    }

    private static float calculateFlashStrength(ServerWorld world, Vec3d flashPos, PlayerEntity player) {
        // 计算距离因子
        float distanceFactor = 1 - (float) (player.getPos().distanceTo(flashPos) / FLASH_RANGE);

        // 计算角度因子
        Vec3d toFlash = flashPos.subtract(player.getEyePos()).normalize();
        Vec3d lookVec = player.getRotationVec(1.0f);
        float angle = (float) Math.toDegrees(Math.acos(lookVec.dotProduct(toFlash)));
        float angleFactor = 1f - Math.min(1, angle / MAX_ANGLE);
        if (angleFactor == 0) return 0;

        // 墙体阻挡因子
        float blockFactor = isBlocked(world, flashPos, player) ? 0.1f : 1f;

        // 玩家状态因子
        float stateFactor = calculateSusceptibility(player);

        return distanceFactor * angleFactor * blockFactor * stateFactor;
    }

    private static void applyPlayerFlash(ServerPlayerEntity player, float impact) {
        if (player == null) return;

        float effectiveStrength = (float) Math.pow(impact, 1.5);
        int duration = 50 + (int) (150 * effectiveStrength);

        ServerPlayNetworking.send(player, new FlashEffectS2CPayload(effectiveStrength));
        player.addStatusEffect(new StatusEffectInstance(
                RPEffect.FLASHED, duration, 0, true, true
        ));
    }
}