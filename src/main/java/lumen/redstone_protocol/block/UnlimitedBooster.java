package lumen.redstone_protocol.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class UnlimitedBooster extends HorizontalBoostBlock {
    public UnlimitedBooster(Settings settings) {
        super(settings, 0.1f);
    }

    @Override
    protected void boostEntity(@NotNull Vec3d horizontalForce, @NotNull Entity entity) {
        entity.addVelocity(horizontalForce);
        entity.velocityModified = true;
    }
}
