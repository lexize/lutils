package org.lexize.lutils.submodules.hud.builders;

import org.moon.figura.utils.caching.CachedType;

public abstract class HUDRenderTaskBuilder<T extends HUDRenderTaskBuilder<T>> implements CachedType<T> {
    abstract void replaceNullWithDefaults();
    abstract void draw();
}
