package org.lexize.lutils.submodules.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.moon.figura.utils.caching.CacheUtils;

public class DisableScissorTask extends HUDRenderTask{

    private static final CacheUtils.Cache<DisableScissorTask> CACHE = CacheUtils.getCache(DisableScissorTask::new, 100);

    @Override
    public void render(MatrixStack matrixStack) {
        RenderSystem.disableScissor();
    }

    @Override
    public HUDRenderTask reset() {
        return this;
    }

    @Override
    public void free() {
        CACHE.offerOld(this);
    }

    public static DisableScissorTask of() {
        return CACHE.getFresh();
    }
}
