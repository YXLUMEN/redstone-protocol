package lumen.redstone_protocol.mixin;

import lumen.redstone_protocol.block.BiologicalFieldBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lumen.redstone_protocol.block.BiologicalFieldBlock.PROTECTION_RADIUS;

@Mixin(ServerPlayerEntity.class)
public abstract class BiologicalFieldProtect {
    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void onPlayerDeath(DamageSource source, CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld serverWorld = player.getServerWorld();
        BlockPos playerBlockPos = player.getBlockPos();

        for (BlockPos pos : BlockPos.iterate(
                playerBlockPos.add(-PROTECTION_RADIUS, -PROTECTION_RADIUS, -PROTECTION_RADIUS),
                playerBlockPos.add(PROTECTION_RADIUS, PROTECTION_RADIUS, PROTECTION_RADIUS))) {
            if (serverWorld.getBlockState(pos).getBlock() instanceof BiologicalFieldBlock) {
                player.setHealth(1.0f);
                info.cancel();
                break;
            }
        }
    }
}
