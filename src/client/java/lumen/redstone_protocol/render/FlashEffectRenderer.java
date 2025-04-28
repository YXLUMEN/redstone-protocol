package lumen.redstone_protocol.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class FlashEffectRenderer {
    private static final Object LOCK = new Object();
    private static final int BASE_DURATION = 6000;
    private static final float MAX_STRENGTH = 2.0f;
    private static final float MIN_ALPHA = 0.01f;

    private static float blurFactor;
    private static float currentStrength = 0;
    private static long currentEndTime = 0;

    public static void handleFlashEffect(float strength) {
        synchronized (LOCK) {
            float adjustedStrength = (float) Math.pow(MathHelper.clamp(strength, 0, 1), 0.7);
            blurFactor = Math.min(0.5f, strength);
            currentStrength = Math.max(currentStrength, adjustedStrength * MAX_STRENGTH);
            currentEndTime = Util.getMeasuringTimeMs() + (long) (BASE_DURATION * currentStrength);
        }
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (currentStrength <= 0) return;

        final float renderStrength;
        final long renderEndTime;

        synchronized (LOCK) {
            renderStrength = currentStrength;
            renderEndTime = currentEndTime;
        }

        long remaining = renderEndTime - Util.getMeasuringTimeMs();

        if (remaining <= 0) {
            synchronized (LOCK) {
                currentStrength = 0;
            }
            return;
        }

        float progress = 1 - (remaining / (BASE_DURATION * renderStrength));
        float alpha = renderStrength * (float) Math.pow(1 - progress, 0.5);

        if (alpha > MIN_ALPHA) {
            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();
            context.fill(0, 0, width, height, 600, getFlashColor(alpha));
            MinecraftClient.getInstance().gameRenderer.renderBlur(blurFactor * alpha);
        } else synchronized (LOCK) {
            currentStrength = 0;
        }
    }

    private static int getFlashColor(float alpha) {
        int alphaByte = MathHelper.clamp((int) (alpha * 255), 0, 255);
        return (alphaByte << 24); // 0xFFFFFF
    }
}
