package lumen.redstone_protocol;

import lumen.redstone_protocol.block.RPBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class RedstoneProtocolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                RPBlocks.LOW_FRICTION_GLASS
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
                RPBlocks.CONTROLLABLE_TRANSPARENT_STONE
        );
    }
}