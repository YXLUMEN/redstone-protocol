package lumen.redstone_protocol.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class LowFriction extends StatusEffect {
    protected LowFriction() {
        super(StatusEffectCategory.BENEFICIAL, 0x069dbf);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setNoDrag(true);
        return super.applyUpdateEffect(entity, amplifier);
    }
}
