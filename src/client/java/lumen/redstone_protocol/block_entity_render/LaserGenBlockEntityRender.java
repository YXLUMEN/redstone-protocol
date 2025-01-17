package lumen.redstone_protocol.block_entity_render;

import lumen.redstone_protocol.block_entity.LaserGenEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.RotationAxis;

public class LaserGenBlockEntityRender implements BlockEntityRenderer<LaserGenEntity> {
    private final TextRenderer textRenderer;

    public LaserGenBlockEntityRender(BlockEntityRendererFactory.Context context) {
        textRenderer = context.getTextRenderer();
        if (textRenderer == null) {
            throw new NullPointerException("TextRenderer is null");
        }
    }

    @Override
    public void render(LaserGenEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Check if player is looking at this block
        if (!(client.crosshairTarget instanceof BlockHitResult blockHit) || !entity.getPos().equals(blockHit.getBlockPos())) {
            return;
        }

        matrices.push();
        try {
            matrices.translate(0.5, 1, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));

            float scale = 0.06f;
            matrices.scale(scale, scale, scale);

            String text = String.valueOf(entity.getCurrentMode());
            int textWidth = textRenderer.getWidth(text);

            textRenderer.draw(
                    text,
                    -textWidth / 2f + 0.5f,
                    -3.5f,
                    0xffffff,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    0,
                    light
            );
        } finally {
            matrices.pop();
        }
    }

    @Override
    public int getRenderDistance() {
        return 12;
    }
}
