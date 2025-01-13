package lumen.redstone_protocol.block;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class RPBlocks {
    public static final Block LOW_FRICTION_GLASS = RPBlocks.register(
            "low_friction_glass",
            new TransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .slipperiness(0.989f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ),
            true
    );

    public static final Block IDEAL_ORBIT = RPBlocks.register(
            "ideal_orbit",
            new Block(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.METAL)
                    .strength(1.2f)
                    .requiresTool()
                    .slipperiness(0.999f)
                    .velocityMultiplier(1.1f)
            ),
            true
    );

    public static final Block PLAYER_TRANSPARENT_BLOCK = RPBlocks.register(
            "player_transparent_block",
            new SelectTransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    PlayerEntity.class
            ), true
    );
    public static final Block MOB_TRANSPARENT_BLOCK = RPBlocks.register(
            "mob_transparent_block",
            new SelectTransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    MobEntity.class
            ), true
    );
    public static final Block ITEM_TRANSPARENT_BLOCK = RPBlocks.register(
            "item_transparent_block",
            new SelectTransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    ItemEntity.class
            ), true
    );

    public static final Block CONTROLLABLE_TRANSPARENT_BLOCK = RPBlocks.register(
            "controllable_transparent_block",
            new ControllableTransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ), true
    );

    public static final Block REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK = RPBlocks.register(
            "reverse_controllable_transparent_block",
            new ReverseControllableTransparentBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .strength(0.5f)
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ), true
    );

    public static final Block CONTROLLABLE_TRANSPARENT_STONE = RPBlocks.register(
            "controllable_transparent_stone",
            new ControllableTransparentBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .nonOpaque()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(2.0f, 6.0f)
            ), true
    );

    public static final Block PRIMARY_SPEED_MULTIPLIER = RPBlocks.register(
            "primary_speed_multiplier",
            new CarpetBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly()
                    .velocityMultiplier(1.2f)),
            true
    );
    public static final Block PRIMARY_HORIZONTAL_BOOSTER = RPBlocks.register(
            "primary_horizontal_booster",
            new HorizontalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly(),
                    0.2f
            ), true
    );
    public static final Block PRIMARY_VERTICAL_BOOSTER = RPBlocks.register(
            "primary_vertical_booster",
            new VerticalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .strength(0.5f)
                    .noCollision()
                    .nonOpaque()
                    .solid()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    0.2f
            ), true
    );

    public static final Block INTERMEDIATE_SPEED_MULTIPLIER = RPBlocks.register(
            "intermediate_speed_multiplier",
            new CarpetBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly()
                    .velocityMultiplier(1.4f)),
            true
    );
    public static final Block INTERMEDIATE_HORIZONTAL_BOOSTER = RPBlocks.register(
            "intermediate_horizontal_booster",
            new HorizontalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly(),
                    0.4f
            ), true
    );
    public static final Block INTERMEDIATE_VERTICAL_BOOSTER = RPBlocks.register(
            "intermediate_vertical_booster",
            new VerticalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .strength(0.5f)
                    .noCollision()
                    .nonOpaque()
                    .solid()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    0.4f
            ), true
    );

    public static final Block ADVANCED_SPEED_MULTIPLIER = RPBlocks.register(
            "advanced_speed_multiplier",
            new CarpetBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly()
                    .velocityMultiplier(1.6f)),
            true
    );
    public static final Block ADVANCED_HORIZONTAL_BOOSTER = RPBlocks.register(
            "advanced_horizontal_booster",
            new HorizontalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly(),
                    0.6f
            ), true
    );
    public static final Block ADVANCED_VERTICAL_BOOSTER = RPBlocks.register(
            "advanced_vertical_booster",
            new VerticalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .strength(0.5f)
                    .noCollision()
                    .nonOpaque()
                    .solid()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never),
                    0.6f
            ), true
    );

    public static final Block UNLIMITED_BOOSTER = RPBlocks.register(
            "unlimited_booster",
            new UnlimitedBooster(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly()
            ), true
    );

    public static final Block CONTROLLABLE_HORIZONTAL_BOOST_BLOCK = RPBlocks.register(
            "controllable_horizontal_booster",
            new ControllableHorizontalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .noCollision()
                    .breakInstantly()
            ), true
    );

    public static final Block CONTROLLABLE_VERTICAL_BOOST_BLOCK = RPBlocks.register(
            "controllable_vertical_booster",
            new ControllableVerticalBoostBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .strength(0.5f)
                    .noCollision()
                    .nonOpaque()
                    .solid()
                    .allowsSpawning(Blocks::never)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ), true
    );

    public static final Block JUMP_ENHANCER = RPBlocks.register(
            "jump_enhancer",
            new CarpetBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.SLIME)
                    .nonOpaque()
                    .breakInstantly()
                    .jumpVelocityMultiplier(3.5f)
            ), true
    );

    // 物流整理
    public static final Block ITEM_PACKER = RPBlocks.register(
            "item_packer",
            new ItemPackerBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(0.8f)
                    .noCollision()
            ), true
    );

    public static final Block ITEM_UNPACKER = RPBlocks.register(
            "item_unpacker",
            new ItemUnpackerBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(0.8f)
                    .noCollision()
            ), true
    );

    public static final Block ITEM_CALIBRATOR = RPBlocks.register(
            "item_calibrator",
            new ItemCalibratorBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(0.8f)
                    .noCollision()), true
    );

    public static final Block ITEM_STEERING_GEAR = RPBlocks.register(
            "item_steering_gear",
            new ItemSteeringGearBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(0.8f)
                    .noCollision()), true
    );

    public static final Block TRACTOR = RPBlocks.register(
            "tractor",
            new TractorBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.2f)
                    .requiresTool()
            ), true
    );

    public static final Block LASER_GENERATOR = RPBlocks.register(
            "laser_generator",
            new LaserGeneratorBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.2f)
                    .requiresTool()
            ), true
    );

    public static final Block TRACTOR_FORCE = RPBlocks.register(
            "laser",
            new LaserBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GLASS)
                    .dropsNothing()
                    .nonOpaque()
                    .noCollision()
                    .strength(-1f, 100f)
                    .solidBlock(Blocks::never)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
            ), false
    );

    public static Block register(String path, Block block, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(RedstoneProtocol.MOD_ID, path);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void initialize() {
    }
}
