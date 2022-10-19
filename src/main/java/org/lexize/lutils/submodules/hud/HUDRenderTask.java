package org.lexize.lutils.submodules.hud;

import net.minecraft.client.util.math.MatrixStack;
import org.lexize.lutils.submodules.hud.builders.HUDRenderTaskBuilder;
import org.moon.figura.utils.caching.CacheUtils;
import org.moon.figura.utils.caching.CachedType;

public abstract class HUDRenderTask implements CachedType<HUDRenderTask>{

    public abstract void render(MatrixStack matrixStack);

}
