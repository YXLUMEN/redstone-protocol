package lumen.redstone_protocol.mixin.client;

import lumen.redstone_protocol.entities.RPEntities;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderers.class)
public abstract class EntityRenderersMixin {
    @Shadow
    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
    }

    @Inject(method = "<clinit>" , at = @At("TAIL"))
    private static void initExtraStaticValue(CallbackInfo ci) {
        register(RPEntities.SMOKE_GRENADE_ENTITY, FlyingItemEntityRenderer::new);
        register(RPEntities.FLASH_GRENADE_ENTITY, FlyingItemEntityRenderer::new);
        register(RPEntities.INCENDIARY_GRENADE_ENTITY, FlyingItemEntityRenderer::new);
        register(RPEntities.FRAG_GRENADE_ENTITY, FlyingItemEntityRenderer::new);
        register(RPEntities.HOWITZER_152_ENTITY, FlyingItemEntityRenderer::new);
    }
}
