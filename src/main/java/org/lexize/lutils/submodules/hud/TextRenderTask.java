package org.lexize.lutils.submodules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.utils.caching.CacheUtils;

public class TextRenderTask extends HUDRenderTask {

    private static final CacheUtils.Cache<TextRenderTask> CACHE = CacheUtils.getCache(TextRenderTask::new, 300);

    public String text;
    public float x,y,z;
    public int color;
    public boolean shadow;
    public boolean mirror;
    public float size;
    private final TextRenderer textRenderer;

    public TextRenderTask() {
        textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public void construct(String text, FiguraVec3 pos, FiguraVec3 color,
                          Boolean shadow, Boolean mirror, Float size) {
        this.text = text;
        x = (float) pos.x;
        y = (float) pos.y;
        z = (float) pos.z;
        int r, g, b;
        r = (int)(color.x*255f);
        g = (int)(color.y*255f);
        b = (int)(color.z*255f);
        this.color = (b & 0xFF) + ((g & 0xFF) << 8) + ((r & 0xFF) << 16);
        this.shadow = shadow;
        this.mirror = mirror;
        this.size = size;
    }

    @Override
    public void render(MatrixStack matrixStack) {
        matrixStack.push();
        matrixStack.translate(x,y,z);
        matrixStack.scale(size,size,size);

        Matrix4f matrix = matrixStack.peek().getPositionMatrix().copy();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        String[] textLines = text.split("\n");
        for (int i = 0; i < textLines.length; i++) {
            textRenderer.draw(textLines[i], 0, (textRenderer.fontHeight * i), color, shadow, matrix, immediate, false, 0, 15728880, mirror);
        }
        immediate.draw();

        matrixStack.pop();
    }

    @Override
    public HUDRenderTask reset() {
        return this;
    }

    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    public static TextRenderTask of() {
        return CACHE.getFresh();
    }
}
