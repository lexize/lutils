package org.lexize.lutils.providers;

import org.lexize.lutils.streams.LInputStream;
import org.moon.figura.lua.LuaWhitelist;

public abstract class LProvider<T> {
    @LuaWhitelist
    public abstract LInputStream getStream(Object source);
}
