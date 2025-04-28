package lumen.redstone_protocol.item;

import lumen.redstone_protocol.entities.IncendiaryGrenadeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class IncendiaryGrenadeItem extends Item implements ProjectileItem {
    private static final int COOLDOWN_TICKS = 20 * 2;

    public IncendiaryGrenadeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(itemStack);
        }

        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!world.isClient) {
            float speed = user.isSneaking() ? 0.5F : 1.5F;
            IncendiaryGrenadeEntity incendiaryGrenadeEntity = new IncendiaryGrenadeEntity(world, user);
            incendiaryGrenadeEntity.setItem(itemStack);
            incendiaryGrenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, speed, 1.0F);
            world.spawnEntity(incendiaryGrenadeEntity);
        }

        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        IncendiaryGrenadeEntity incendiaryGrenadeEntity = new IncendiaryGrenadeEntity(world, pos.getX(), pos.getY(), pos.getZ());
        incendiaryGrenadeEntity.setItem(stack);
        return incendiaryGrenadeEntity;
    }
}
