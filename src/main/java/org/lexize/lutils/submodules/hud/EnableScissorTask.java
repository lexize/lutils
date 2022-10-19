package org.lexize.lutils.submodules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.moon.figura.utils.caching.CacheUtils;
import org.moon.figura.utils.ui.UIHelper;

public class EnableScissorTask extends HUDRenderTask{

    private float x,y,width,height;

    private static final CacheUtils.Cache<EnableScissorTask> CACHE = CacheUtils.getCache(EnableScissorTask::new, 100);

    @Override
    public void render(MatrixStack matrixStack) {
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        int screenY = MinecraftClient.getInstance().getWindow().getFramebufferHeight();

        int scaledWidth = (int) Math.max(width * scale, 0);
        int scaledHeight = (int) Math.max(height * scale, 0);
        RenderSystem.enableScissor((int) (x * scale), (int) (screenY - y * scale - scaledHeight), scaledWidth, scaledHeight);
    }

    @Override
    public HUDRenderTask reset() {
        return this;
    }

    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    public static EnableScissorTask of(float x, float y, float width, float height) {
        var task = CACHE.getFresh();
        task.x = x;
        task.y = y;
        task.width = width;
        task.height = height;
        return task;
    }
}
