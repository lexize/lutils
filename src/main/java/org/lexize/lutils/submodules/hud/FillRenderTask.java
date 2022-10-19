package org.lexize.lutils.submodules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lexize.lutils.submodules.LUtilsHUD;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;
import org.moon.figura.utils.caching.CacheUtils;
import org.moon.figura.utils.caching.CachedType;

public class FillRenderTask extends HUDRenderTask {

    private static final CacheUtils.Cache<FillRenderTask> CACHE = CacheUtils.getCache(FillRenderTask::new, 300);

    public float x1,x2,y1,y2,z;
    public float r,g,b,a;

    public FillRenderTask() {

    }

    public void construct(FiguraVec3 pos, FiguraVec2 size, FiguraVec4 color) {
        x1 = (float) (pos.x      );
        y1 = (float) (pos.y      );
        z  = (float) (pos.z      );
        x2 = (float) (x1 + size.x);
        y2 = (float) (y1 + size.y);

        r = (float)(color.x);
        g = (float)(color.y);
        b = (float)(color.z);
        a = (float)(color.w);
    }

    @Override
    public void render(MatrixStack matrixStack) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y2, z).color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x2, y2, z).color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x2, y1, z).color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x1, y1, z).color(r,g,b,a).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public HUDRenderTask reset() {
        return this;
    }

    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    public static FillRenderTask of() {
        return CACHE.getFresh();
    }
}
