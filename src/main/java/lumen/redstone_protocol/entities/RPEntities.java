package lumen.redstone_protocol.entities;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RPEntities {
    public static final EntityType<SmokeGrenadeEntity> SMOKE_GRENADE_ENTITY = register(
            "smoke_grenade",
            EntityType.Builder.<SmokeGrenadeEntity>create(SmokeGrenadeEntity::new, SpawnGroup.MISC)
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

    public static final EntityType<FlashGrenadeEntity> FLASH_GRENADE_ENTITY = register(
            "flash_grenade",
            EntityType.Builder.<FlashGrenadeEntity>create(FlashGrenadeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.3f, 0.4f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    public static final EntityType<IncendiaryGrenadeEntity> INCENDIARY_GRENADE_ENTITY = register(
            "incendiary_grenade",
            EntityType.Builder.<IncendiaryGrenadeEntity>create(IncendiaryGrenadeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.3f, 0.4f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    public static final EntityType<FragGrenadeEntity> FRAG_GRENADE_ENTITY = register(
            "frag_grenade",
            EntityType.Builder.<FragGrenadeEntity>create(FragGrenadeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.35f, 0.35f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    public static final EntityType<Howitzer152Entity> HOWITZER_152_ENTITY = register(
            "howitzer152",
            EntityType.Builder.<Howitzer152Entity>create(Howitzer152Entity::new, SpawnGroup.MISC)
                    .dimensions(1.0f, 1.0f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(RedstoneProtocol.MOD_ID, id), builder.build());
    }

    public static void registerEntities() {
    }
}

