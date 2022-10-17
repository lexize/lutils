package org.lexize.lutils.submodules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;

public class TextureRenderTask implements HUDRenderTask {

    public float x0,y0,x1,y1,z;
    public float u0,v0,u1,v1;
    public float r,g,b,a;
    public Identifier texture_id;

    public TextureRenderTask() {

    }

    public void construct(FiguraVec3 pos,
                          FiguraVec2 size,
                          FiguraVec2 uv0,
                          FiguraVec2 uv1,
                          Identifier texture,
                          FiguraVec4 color) {
        x0 = (float) pos.x;
        y0 = (float) pos.y;
        x1 = (float) (pos.x + size.x);
        y1 = (float) (pos.y + size.y);
        z = (float) pos.z;

        u0 = (float) uv0.x;
        v0 = (float) uv0.y;
        u1 = (float) uv1.x;
        v1 = (float) uv1.y;

        texture_id = texture;

        r = (float) color.x;
        g = (float) color.y;
        b = (float) color.z;
        a = (float) color.w;
    }

    @Override
    public void render(MatrixStack matrixStack) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix().copy();
        RenderSystem.setShaderTexture(0, texture_id);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix, x0, y1, z)
                .texture(u0, v1)
                .color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x1, y1, z)
                .texture(u1, v1)
                .color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x1, y0, z)
                .texture(u1, v0)
                .color(r,g,b,a).next();
        bufferBuilder.vertex(matrix, x0, y0, z)
                .texture(u0, v0)
                .color(r,g,b,a).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.disableBlend();
    }
}
