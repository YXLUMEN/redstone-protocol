package lumen.redstone_protocol.item;

import lumen.redstone_protocol.util.TrajectoryRayCaster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DebugGunItem extends Item {
    public DebugGunItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world instanceof ServerWorld serverWorld) {
            float yaw = user.getYaw();
            float pitch = user.getPitch();

            float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
            float g = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));
            float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));

            Vec3d manualVec = new Vec3d(f, g, h).normalize();

            TrajectoryRayCaster rayCaster = new TrajectoryRayCaster(serverWorld, user)
                    .withBaseRayLength(64)
                    .withBounceChance(user.isSneaking() ? 0.3f : 2.0f)
                    .withPenetrateChance(user.isSneaking() ? 2.0f : 1.0f)
                    .withMaxBounces((short) 2);

            rayCaster.rayCast(user.getEyePos(), manualVec, 1.0f, true);

            serverWorld.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS);
        }

        return TypedActionResult.consume(itemStack);
    }
}
