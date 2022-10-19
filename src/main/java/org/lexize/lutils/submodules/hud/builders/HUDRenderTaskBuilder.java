package org.lexize.lutils.submodules.hud.builders;

import org.lexize.lutils.submodules.hud.HUDRenderTask;
import org.moon.figura.utils.caching.CachedType;

public abstract class HUDRenderTaskBuilder {
    abstract void replaceNullWithDefaults();
    abstract void draw();

}
