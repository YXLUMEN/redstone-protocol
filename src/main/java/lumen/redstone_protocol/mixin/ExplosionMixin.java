package lumen.redstone_protocol.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import lumen.redstone_protocol.block.ExplosionAbsorberBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow
    @Final
    private World world;

    @Unique
    private boolean explosionBlocked = false;

    @Inject(
            method = "collectBlocksAndDamageEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"),
            cancellable = true
    )
    private void onBlockCheck(CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.getBlock() instanceof ExplosionAbsorberBlock) {
            ci.cancel();
            this.explosionBlocked = true;
        }
    }

    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    public void onAffectWorld(boolean particles, CallbackInfo ci) {
        if (this.explosionBlocked) {
            ci.cancel();
        }
    }
}
