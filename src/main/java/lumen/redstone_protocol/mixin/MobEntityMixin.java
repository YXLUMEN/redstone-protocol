package lumen.redstone_protocol.mixin;

import lumen.redstone_protocol.effect.RPEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void preventTargetingInSmoke(LivingEntity target, CallbackInfo ci) {
        if (target != null && target.hasStatusEffect(RPEffects.SMOKE_CLOAK)) {
            ci.cancel();
        }
    }
}
