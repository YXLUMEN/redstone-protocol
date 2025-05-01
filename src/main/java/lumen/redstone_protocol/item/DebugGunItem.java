package lumen.redstone_protocol.item;

import lumen.redstone_protocol.util.TrajectoryRayCaster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

        float yaw = user.getYaw();
        float pitch = user.getPitch();

        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));

        Vec3d manualVec = new Vec3d(f, g, h).normalize();

        TrajectoryRayCaster.rayCast(world, user, user.getEyePos(), manualVec, 2.0f, user.isSneaking() ? 1.5f : 0.3f);

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
