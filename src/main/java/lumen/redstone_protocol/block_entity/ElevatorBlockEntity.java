package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.block.ElevatorBlock;
import lumen.redstone_protocol.util.ElevatorCooldownHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElevatorBlockEntity extends BlockEntity {
    public static final short ACTIVE_RADIUS = 72;
    public static final short TELEPORT_COOLDOWN = 15;

    private final Box detectionBox = new Box(pos).expand(0, 0.5, 0);

    public ElevatorBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.ELEVATOR_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ElevatorBlockEntity blockEntity) {
        if (world == null || world.isClient) return;
        ServerWorld serverWorld = (ServerWorld) world;

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, blockEntity.detectionBox, playerEntity -> true);
        for (PlayerEntity player : players) {
            if (player == null || ElevatorCooldownHandler.isOnCooldown(player)) continue;

            if (player.isSneaking()) {
                BlockPos target = searchElevator(serverWorld, blockPos, false);
                if (target != null) {
                    player.requestTeleport(target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5);
                    ElevatorCooldownHandler.setCooldown(player, TELEPORT_COOLDOWN);
                    continue;
                }
            }
            if (isPlayerJumping(player)) {
                BlockPos target = searchElevator(serverWorld, blockPos, true);
                if (target != null) {
                    player.requestTeleport(target.getX() + 0.5, target.getY() + 1, target.getZ() + 0.5);
                    ElevatorCooldownHandler.setCooldown(player, TELEPORT_COOLDOWN);
                }
            }
        }
    }

    @Nullable
    private static BlockPos searchElevator(ServerWorld world, BlockPos blockPos, boolean up) {
        int startY = up ? blockPos.getY() + 1 : blockPos.getY() - 1;
        int endY = up ? blockPos.getY() + ACTIVE_RADIUS : blockPos.getY() - ACTIVE_RADIUS;

        if (up) {
            for (int y = startY; y <= endY; y++) {
                BlockPos pos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
                if (world.getBlockState(pos).getBlock() instanceof ElevatorBlock && isSafeForTeleport(world, pos)) {
                    return pos;
                }
            }
        } else {
            for (int y = startY; y >= endY; y--) {
                BlockPos pos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
                if (world.getBlockState(pos).getBlock() instanceof ElevatorBlock && isSafeForTeleport(world, pos)) {
                    return pos;
                }
            }
        }
        return null;
    }

    private static boolean isSafeForTeleport(ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos.up(1)).isReplaceable() && world.getBlockState(pos.up(2)).isReplaceable();
    }

    private static boolean isPlayerJumping(PlayerEntity player) {
        return player.getVelocity().y > 0.1;
    }
}
