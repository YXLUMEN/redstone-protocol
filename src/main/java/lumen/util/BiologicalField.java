package lumen.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class BiologicalField {
    private static BiologicalFieldState state;

    public static final float PROTECTION_RADIUS = 6.0f;

    public static void loadFromWorld(ServerWorld world) {
        state = BiologicalFieldState.getServerState(world.getServer());
    }

    public static void addFieldPos(BlockPos pos) {
        if (state != null) {
            state.addFieldPos(pos);
        }
    }

    public static void removeFieldPos(BlockPos pos) {
        if (state != null) {
            state.removeFieldPos(pos);
        }
    }

    public static boolean isPlayerProtected(PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        for (BlockPos pos : state.getFieldPositions()) {
            Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            if (center.distanceTo(playerPos) <= PROTECTION_RADIUS) return true;
        }
        return false;
    }
}
