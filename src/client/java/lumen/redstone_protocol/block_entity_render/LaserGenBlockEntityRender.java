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
    public static final float TEXT_SCALE = 0.06f;

    private final TextRenderer textRenderer;

    public LaserGenBlockEntityRender(BlockEntityRendererFactory.Context context) {
        this.textRenderer = context.getTextRenderer();
        if (this.textRenderer == null) {
            throw new NullPointerException("Can't get TextRenderer for LaserGenBlock!");
        }
    }

    @Override
    public void render(LaserGenEntity laserGenEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Check if player is looking at this block
        if (!(client.crosshairTarget instanceof BlockHitResult blockHit) || !laserGenEntity.getPos().equals(blockHit.getBlockPos())) {
            return;
        }

        String text = String.valueOf(laserGenEntity.getCurrentMode());
        int textWidth = this.textRenderer.getWidth(text);

        matrices.push();
        matrices.translate(0.5, 1.05f, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrices.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

        this.textRenderer.draw(
                text,
                -textWidth / 2f,
                -4f,
                0xffffff,
                false,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.SEE_THROUGH,
                0,
                light
        );
        matrices.pop();
    }

    @Override
    public int getRenderDistance() {
        return 12;
    }
}
