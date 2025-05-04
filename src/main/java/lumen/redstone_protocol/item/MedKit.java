package lumen.redstone_protocol.item;

import lumen.redstone_protocol.network.SoundInterruptS2CPayload;
import lumen.redstone_protocol.sounds.RPSoundEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import static lumen.redstone_protocol.item.Battery.spawnChargingParticles;

public class MedKit extends Item {
    private static final int BASE_COOLDOWN_TICKS = 20;

    private final int useTime;

    public MedKit(Settings settings, int useTime) {
        super(settings);
        this.useTime = useTime;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user.getHealth() >= user.getMaxHealth()) {
            return TypedActionResult.fail(itemStack);
        }

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(itemStack);
        }

        user.getItemCooldownManager().set(this, BASE_COOLDOWN_TICKS + useTime);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) {
            spawnChargingParticles(world, user, new DustParticleEffect(DustParticleEffect.RED, 1), 1);
            return;
        }

        if (remainingUseTicks == useTime - 4) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    RPSoundEvents.KIT_USING, SoundCategory.PLAYERS);
            return;
        }
        if (remainingUseTicks == useTime - 8) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    RPSoundEvents.KIT_START, SoundCategory.PLAYERS);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    RPSoundEvents.KIT_FINISH, SoundCategory.PLAYERS, 1.5f, 1.0f);

            user.setHealth(user.getMaxHealth());
        }

        stack.decrementUnlessCreative(1, user);
        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (remainingUseTicks <= 0) return;

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                RPSoundEvents.KIT_FAIL, SoundCategory.PLAYERS);
        if (user instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new SoundInterruptS2CPayload(2));
            player.getItemCooldownManager().set(this, 0);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return useTime;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
