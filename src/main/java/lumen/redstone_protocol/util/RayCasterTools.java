package lumen.redstone_protocol.util;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// debug
public class RayCasterTools {
    public static void debugDrawRay(World world, Vec3d start, Vec3d end, SimpleParticleType particleType) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        Vec3d direction = end.subtract(start);
        double length = direction.length();
        direction = direction.normalize();

        int particleCount = (int) (length * 1);
        if (particleCount <= 0) return;

        double step = length / particleCount;

        for (int i = 0; i <= particleCount; i++) {
            Vec3d pos = start.add(direction.multiply(i * step));
            serverWorld.spawnParticles(
                    particleType,
                    pos.x, pos.y, pos.z,
                    1,
                    0, 0, 0,
                    0
            );
        }
    }
}
