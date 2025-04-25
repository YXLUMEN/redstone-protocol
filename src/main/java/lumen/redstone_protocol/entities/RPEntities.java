package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RPEntities {
    public static final EntityType<SmokeBombEntity> SMOKE_BOMB = register(
            "smoke_bomb",
            EntityType.Builder.<SmokeBombEntity>create(SmokeBombEntity::new, SpawnGroup.MISC)
                    .dimensions(0.3f, 0.4f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    public static final EntityType<SmokeEffectAreaEntity> SMOKE_EFFECT_AREA = register(
            "smoke_effect_area",
            EntityType.Builder.<SmokeEffectAreaEntity>create(SmokeEffectAreaEntity::new, SpawnGroup.MISC)
                    .dimensions(0, 0)
                    .maxTrackingRange(0)
    );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(RedstoneProtocol.MOD_ID, id), builder.build());
    }

    public static void registerEntities() {
    }
}

