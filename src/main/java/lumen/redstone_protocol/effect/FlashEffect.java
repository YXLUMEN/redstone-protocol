package lumen.redstone_protocol.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class FlashEffect extends StatusEffect {
    public FlashEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFFFFF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof MobEntity mob) {
            mob.setTarget(null);
            if (mob.getNavigation() != null) {
                mob.getNavigation().stop();
            }
        }
        entity.setHeadYaw(entity.getBodyYaw() + 180);

        return super.applyUpdateEffect(entity, amplifier);
    }
}
