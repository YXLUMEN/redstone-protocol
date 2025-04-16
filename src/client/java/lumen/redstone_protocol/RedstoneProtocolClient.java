package lumen.redstone_protocol;

import lumen.redstone_protocol.block.RPBlocks;
import lumen.redstone_protocol.block_entity.RPBlockEntities;
import lumen.redstone_protocol.block_entity_render.LaserGenBlockEntityRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class RedstoneProtocolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                RPBlocks.LOW_FRICTION_GLASS,
                RPBlocks.TRACTOR_FORCE
        );

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                RPBlocks.PRIMARY_SPEED_MULTIPLIER,
                RPBlocks.PRIMARY_HORIZONTAL_BOOSTER,
                RPBlocks.PRIMARY_VERTICAL_BOOSTER,

                RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER,
                RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER,
                RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER,

                RPBlocks.ADVANCED_SPEED_MULTIPLIER,
                RPBlocks.ADVANCED_HORIZONTAL_BOOSTER,
                RPBlocks.ADVANCED_VERTICAL_BOOSTER,
                RPBlocks.UNLIMITED_BOOSTER,

                RPBlocks.CONTROLLABLE_HORIZONTAL_BOOST_BLOCK,
                RPBlocks.CONTROLLABLE_VERTICAL_BOOST_BLOCK,
                RPBlocks.ITEM_PACKER,
                RPBlocks.ITEM_UNPACKER,

                RPBlocks.PLAYER_TRANSPARENT_BLOCK,
                RPBlocks.MOB_TRANSPARENT_BLOCK,
                RPBlocks.ITEM_TRANSPARENT_BLOCK,
                RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK,
                RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK,
                RPBlocks.CONTROLLABLE_TRANSPARENT_STONE,
                RPBlocks.ACTIVE_DEFENSE,
                RPBlocks.PROJECTILE_REDUCER_BLOCK
        );

        BlockEntityRendererFactories.register(RPBlockEntities.LASER_GEN_ENTITY, LaserGenBlockEntityRender::new);
    }
}